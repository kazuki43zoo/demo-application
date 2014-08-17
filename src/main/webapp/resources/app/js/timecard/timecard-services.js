(function() {
    'use strict';

    var BASE_WORKING_MINUTE_PER_DAY = 7.75 * 60;
    var PENALTY_DAYS = 3;
    var TIME_UNIT_MINUTE_OF_LIQUIDATION_TIME = 30;

    var TimeCardResource;
    var DailyAttendanceResource;

    function TimeCardService(InjectedTimeCardResource, InjectedDailyAttendanceResource, $http,
            dateTimeService, apiBasePath) {
        TimeCardResource = InjectedTimeCardResource;
        DailyAttendanceResource = InjectedDailyAttendanceResource;
        this.$http = $http;
        this.dateTimeService = dateTimeService;
        this.apiBasePath = apiBasePath;
    }

    TimeCardService.prototype.initTotal = function(total) {
        total.actualWorkingMinute = 0;
        total.compensationMinute = 0;
        total.midnightWorkingMinute = 0;
        total.tardyOrEarlyLeavingCount = 0;
        total.tardyOrEarlyLeavingPenaltyCount = 0;
        total.absenceCount = 0;
        total.paidLeaveCount = 0;
        total.baseWorkDays = 0;
        total.baseWorkingMinute = 0;
        total.liquidationMinute = 0;
        total.overtimeTime = 0;
        total.penaltyTime = 0;
    };

    TimeCardService.prototype.addCalculateResult = function(attendance, total) {
        total.actualWorkingMinute += attendance.actualWorkingMinute;
        total.compensationMinute += attendance.compensationMinute;
        total.midnightWorkingMinute += attendance.midnightWorkingMinute;
        if (attendance.paidLeave === true) {
            total.paidLeaveCount++;
        }
        if (attendance.tardyOrEarlyLeaving === true) {
            total.tardyOrEarlyLeavingCount++;
            if (attendance.specialWorkCode === '') {
                total.tardyOrEarlyLeavingPenaltyCount++;
            }
        }
        if (attendance.absence === true) {
            total.absenceCount++;
        }
        if (!this.dateTimeService.isHoliday(attendance.targetDate)) {
            total.baseWorkDays++;
        }
    }

    TimeCardService.prototype.calculateTotalMinute = function(total) {
        total.baseWorkingMinute = total.baseWorkDays * BASE_WORKING_MINUTE_PER_DAY;
        total.workingMinute = total.actualWorkingMinute + total.compensationMinute;
        total.overtimeTime = total.workingMinute - total.baseWorkingMinute;
        total.penaltyTime = Math.floor(total.tardyOrEarlyLeavingPenaltyCount / PENALTY_DAYS)
                * (BASE_WORKING_MINUTE_PER_DAY * -1);
        total.liquidationMinute = this.dateTimeService.truncateWithTimeUnit(total.overtimeTime
                + total.penaltyTime, TIME_UNIT_MINUTE_OF_LIQUIDATION_TIME);
    }

    TimeCardService.prototype.getTimeCard = function(targetMonth, total) {
        var timeCard = TimeCardResource.get({
            targetMonth : targetMonth
        });
        var _this = this;
        var reflectTimeCard = function(timeCard) {
            _this.calculateTimeCard(timeCard, total);
        }
        timeCard.$promise.then(reflectTimeCard);
        return timeCard;
    };

    TimeCardService.prototype.calculateTimeCard = function(timeCard, total) {
        this.initTotal(total);
        angular.forEach(timeCard.attendances, function(attendance) {
            this.addCalculateResult(attendance, total);
        }, this);
        this.calculateTotalMinute(total);
    };

    TimeCardService.prototype.initTimeCard = function(timeCard, total) {
        var _this = this;
        var initTimeCard = function(defaultAttendance) {
            _this.initTotal(total);
            delete defaultAttendance.targetDate
            _this.initTimeCardByDefaultAttendance(timeCard, total, defaultAttendance);
            _this.calculateTotalMinute(total);
        }
        return this.$http.get(this.apiBasePath + '/timecards/calculate', {
            params : {
                target : 'default',
                workPlaceUuid : timeCard.workPlaceUuid
            }
        }).success(initTimeCard);
    };

    TimeCardService.prototype.initTimeCardByDefaultAttendance = function(timeCard, total,
            defaultAttendance) {
        angular.forEach(timeCard.attendances, function(attendance) {
            angular.extend(attendance, defaultAttendance);
            if (this.dateTimeService.isHoliday(attendance.targetDate)) {
                attendance.beginTime = null;
                attendance.finishTime = null;
                attendance.actualWorkingMinute = 0;
                attendance.compensationMinute = 0;
            }
            this.addCalculateResult(attendance, total);
        }, this);
    };

    TimeCardService.prototype.calculateTime = function(defaultWorkPlaceUuid, attendance,
            originalAttendance, total, fieldName) {
        if (fieldName != undefined) {
            if (attendance[fieldName] === originalAttendance[fieldName]) {
                return;
            }
        }
        var _this = this;
        var reflectCalculateResult = function(calculatedAttendance) {
            delete calculatedAttendance.workPlaceUuid;
            _this.reflectCalculateResult(calculatedAttendance, originalAttendance, total);
            angular.extend(attendance, calculatedAttendance);
            angular.copy(attendance, originalAttendance);
        }
        var postingAttendance = angular.copy(attendance);
        if (postingAttendance.workPlaceUuid === '') {
            postingAttendance.workPlaceUuid = defaultWorkPlaceUuid;
        }
        return this.$http.post(this.apiBasePath + '/timecards/calculate', postingAttendance)
                .success(reflectCalculateResult);
    };

    TimeCardService.prototype.reflectCalculateResult = function(calculatedAttendance,
            originalAttendance, total) {
        total.actualWorkingMinute += calculateDiff(originalAttendance.actualWorkingMinute,
                calculatedAttendance.actualWorkingMinute);
        total.compensationMinute += calculateDiff(originalAttendance.compensationMinute,
                calculatedAttendance.compensationMinute);
        total.midnightWorkingMinute += calculateDiff(originalAttendance.midnightWorkingMinute,
                calculatedAttendance.midnightWorkingMinute);
        total.paidLeaveCount += calculateCount(originalAttendance.paidLeave,
                calculatedAttendance.paidLeave);
        total.tardyOrEarlyLeavingCount += calculateCount(originalAttendance.tardyOrEarlyLeaving,
                calculatedAttendance.tardyOrEarlyLeaving);
        total.tardyOrEarlyLeavingPenaltyCount += calculateCount(
                (originalAttendance.tardyOrEarlyLeaving === true && originalAttendance.specialWorkCode === ''),
                (calculatedAttendance.tardyOrEarlyLeaving === true && calculatedAttendance.specialWorkCode === ''));
        total.absenceCount += calculateCount(originalAttendance.absence,
                calculatedAttendance.absence);

        this.calculateTotalMinute(total);
    };

    TimeCardService.prototype.saveTimeCard = function(targetMonth, timeCard) {
        return timeCard.$put({
            targetMonth : targetMonth
        });
    };

    TimeCardService.prototype.saveDailyAttendance = function(targetMonth, targetDay, attendance) {
        return DailyAttendanceResource.put({
            targetMonth : targetMonth,
            targetDay : targetDay
        }, attendance).$promise;
    };

    function calculateDiff(before, after) {
        var diff = 0;
        diff -= before;
        diff += after;
        return diff;
    }

    function calculateCount(before, after) {
        var count = 0;
        if (before === true) {
            count--;
        }
        if (after === true) {
            count++;
        }
        return count;
    }

    angular.module('app.usecase').service(
            'timeCardService',
            [ 'TimeCardResource', 'DailyAttendanceResource', '$http', 'dateTimeService',
                    'apiBasePath', TimeCardService ]);

})();
