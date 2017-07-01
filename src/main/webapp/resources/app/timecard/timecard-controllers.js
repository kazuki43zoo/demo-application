(function() {
    'use strict';

    function TimeCardController($location, timeCardService, dateTimeService) {
        this.$location = $location;
        this.timeCardService = timeCardService;
        this.dateTimeService = dateTimeService;

        this.originalAttendance = {};
        this.loading = false;
        this.targetDay = 1;
        this.total = {};
        this.targetMonth = $location.path().substring(1);

        this.timeCardService.initTotal(this.total);
    }

    TimeCardController.prototype.loadTimeCard = function() {
        if (this.targetMonth === '') {
            this.loadToday();
        } else {
            this.loading = true;
            this.timeCard = this.timeCardService.getTimeCard(this.targetMonth, this.total);
            var _this = this;
            var reflectLoadResult = function(timeCard) {
                _this.defaultWorkPlace = angular.copy(timeCard.workPlace);
                _this.stored = timeCard.stored;
                delete timeCard.workPlace;
                delete timeCard.stored;
                _this.$location.path(_this.targetMonth);
                _this.loadedTimeCard = angular.copy(timeCard);
                _this.setEditableAttendance();
                _this.loading = false;
            };
            this.timeCard.$promise.then(reflectLoadResult);
        }
    };

    TimeCardController.prototype.loadToday = function() {
        var _this = this;
        var loadToday = function(currentDateTime) {
            var currentDate = new Date(currentDateTime.dateTime);
            var targetMonth = _this.dateTimeService.formatDate(currentDate, 'yyyy-MM');
            if (targetMonth !== _this.targetMonth) {
                _this.targetMonth = targetMonth;
                _this.targetDay = currentDate.getDate();
                _this.loadTimeCard();
            } else {
                if (_this.targetDay !== currentDate.getDate()) {
                    _this.targetDay = currentDate.getDate();
                    _this.setEditableAttendance();
                }
            }
        };
        this.dateTimeService.getCurrentDateTime().then(loadToday);
    };

    TimeCardController.prototype.setEditableAttendance = function() {
        if (!angular.isNumber(this.targetDay)) {
            return;
        }
        if (this.timeCard.attendances.length < this.targetDay) {
            this.targetDay = this.timeCard.attendances.length;
        }
        var targetDayIndex = this.targetDay - 1;
        this.editableAttendance = this.timeCard.attendances[targetDayIndex];
        this.loadedAttendance = this.loadedTimeCard.attendances[targetDayIndex];
        angular.copy(this.editableAttendance, this.originalAttendance);
    };

    TimeCardController.prototype.needSaveTimeCard = function() {
        return !angular.equals(this.timeCard, this.loadedTimeCard);
    };

    TimeCardController.prototype.needSaveEditableAttendance = function() {
        return !angular.equals(this.editableAttendance, this.loadedAttendance);
    };

    TimeCardController.prototype.initTimeCard = function() {
        var _this = this;
        this.timeCardService.initTimeCard(this.timeCard, this.total).success(function() {
            _this.setEditableAttendance();
        });
    };

    TimeCardController.prototype.saveTimeCard = function() {
        var _this = this;
        var reflectSaveResult = function() {
            _this.stored = true;
            angular.copy(_this.timeCard, _this.loadedTimeCard);
            _this.setEditableAttendance();
        };
        this.timeCardService.saveTimeCard(this.targetMonth, this.timeCard).then(reflectSaveResult);
    };

    TimeCardController.prototype.saveDailyAttendance = function() {
        var _this = this;
        var reflectSaveResult = function() {
            angular.copy(_this.editableAttendance, _this.loadedAttendance);
        };
        this.timeCardService.saveDailyAttendance(this.targetMonth, this.targetDay,
                this.editableAttendance).then(reflectSaveResult);
    };

    TimeCardController.prototype.calculateTime = function(fieldName) {
        this.timeCardService.calculateTime(this.timeCard.workPlaceUuid, this.editableAttendance,
                this.originalAttendance, this.total, fieldName);
    };

    TimeCardController.prototype.enter = function() {
        var _this = this;
        var enter = function(currentDateTime) {
            var currentDate = new Date(currentDateTime.dateTime);
            var time = _this.dateTimeService.formatDate(currentDate, 'HH:mm');
            if (time !== _this.editableAttendance.beginTime) {
                _this.editableAttendance.beginTime = time;
                _this.calculateTime('beginTime');
                _this.saveDailyAttendance();
            }
        };
        this.dateTimeService.getCurrentDateTime().then(enter);
    };

    TimeCardController.prototype.exit = function() {
        var _this = this;
        var exit = function(currentDateTime) {
            var currentDate = new Date(currentDateTime.dateTime);
            var time = _this.dateTimeService.formatDate(currentDate, 'HH:mm');
            if (time !== _this.editableAttendance.finishTime) {
                _this.editableAttendance.finishTime = time;
                _this.calculateTime('finishTime');
                _this.saveDailyAttendance();
            }
        };
        this.dateTimeService.getCurrentDateTime().then(exit);
    };

    angular.module('app.usecase').controller('TimeCardController',
            [ '$location', 'timeCardService', 'dateTimeService', TimeCardController ]);

})();
