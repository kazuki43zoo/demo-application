(function() {
	'use strict';

	var SUNDAY = 0;
	var SATURDAY = 6;

	var DateResource;
	var TimeCardResource;
	var DailyAttendanceResource;

	function TimeCardService(InjectedDateResource, InjectedTimeCardResource,
			InjectedDailyAttendanceResource, $http, $filter) {
		DateResource = InjectedDateResource;
		TimeCardResource = InjectedTimeCardResource;
		DailyAttendanceResource = InjectedDailyAttendanceResource;
		this.$http = $http;
		this.$filter = $filter;
		this.apiBasePath = angular.element("meta[name='contextPath']").attr(
				'content')
				+ '/api/v1';
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
		angular.forEach(timeCard.attendances, function(attendance) {
			this.addCalculateResult(attendance, total);
		}, this);
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
		}
		if (attendance.absence === true) {
			total.absenceCount++;
		}
	}

	TimeCardService.prototype.getCurrentDateTime = function() {
		return DateResource.get({
			dateId : 'currentDateTime'
		}).$promise;
	};

	TimeCardService.prototype.initTimeCard = function(timeCard, total) {
		var _this = this;
		var initTimeCard = function(defaultAttendance) {
			_this.initTimeCardByDefaultAttendance(timeCard, total,
					defaultAttendance);
		}
		return this.$http.get(this.apiBasePath + '/timecards/calculate', {
			params : {
				target : 'default',
				workPlaceUuid : timeCard.workPlaceUuid
			}
		}).success(initTimeCard);
	};

	TimeCardService.prototype.initTimeCardByDefaultAttendance = function(
			timeCard, total, defaultAttendance) {
		this.initTotal(total);
		angular.forEach(timeCard.attendances, function(attendance) {
			var targetDate = attendance.targetDate;
			angular.extend(attendance, defaultAttendance);
			attendance.targetDate = targetDate;
			if (this.isHoliday(targetDate)) {
				attendance.beginTime = null;
				attendance.finishTime = null;
				attendance.actualWorkingMinute = 0;
			}
			this.addCalculateResult(attendance, total);
		}, this);
	};

	TimeCardService.prototype.calculateTime = function(attendance,
			originalAttendance, total, fieldName) {
		if (attendance[fieldName] === originalAttendance[fieldName]) {
			return;
		}
		var _this = this;
		var reflectCalculateResult = function(calculatedAttendance) {
			_this.reflectCalculateResult(calculatedAttendance,
					originalAttendance, total);
			angular.copy(calculatedAttendance, attendance);
			angular.copy(attendance, originalAttendance);
		}
		return this.$http.post(this.apiBasePath + '/timecards/calculate',
				attendance).success(reflectCalculateResult);
	};

	TimeCardService.prototype.reflectCalculateResult = function(
			calculatedAttendance, originalAttendance, total) {
		total.actualWorkingMinute += this.calculateDiff(
				originalAttendance.actualWorkingMinute,
				calculatedAttendance.actualWorkingMinute);
		total.compensationMinute += this.calculateDiff(
				originalAttendance.compensationMinute,
				calculatedAttendance.compensationMinute);
		total.midnightWorkingMinute += this.calculateDiff(
				originalAttendance.midnightWorkingMinute,
				calculatedAttendance.midnightWorkingMinute);
	};

	TimeCardService.prototype.initTotal = function(total) {
		total.actualWorkingMinute = 0;
		total.compensationMinute = 0;
		total.midnightWorkingMinute = 0;
		total.tardyOrEarlyLeavingCount = 0;
		total.absenceCount = 0;
		total.paidLeaveCount = 0;
	};

	TimeCardService.prototype.changePaidLeave = function(attendance, total) {
		if (attendance.paidLeave === true) {
			attendance.beginTime = '09:00';
			attendance.finishTime = '17:45';
			attendance.specialWorkCode = '';
			attendance.workPlaceUuid = '';
			total.paidLeaveCount++;
		} else {
			total.paidLeaveCount--;
		}
	}

	TimeCardService.prototype.saveTimeCard = function(targetMonth, timeCard) {
		var reflectSaveResult = function(timeCard) {
			timeCard.stored = true;
		}
		return timeCard.$put({
			targetMonth : targetMonth
		}).then(reflectSaveResult);
	};

	TimeCardService.prototype.saveDailyAttendance = function(targetMonth,
			targetDay, attendance) {
		return DailyAttendanceResource.put({
			targetMonth : targetMonth,
			targetDay : targetDay
		}, attendance).$promise;
	};

	TimeCardService.prototype.isHoliday = function(targetDate) {
		if (targetDate == undefined) {
			return false;
		}
		var targetDayOfWeek = new Date(targetDate).getDay();
		return targetDayOfWeek === SUNDAY || targetDayOfWeek === SATURDAY;
	};

	TimeCardService.prototype.formatDate = function(date, format) {
		if (date == undefined || date == null) {
			return null;
		}
		return this.$filter('date')(date, format);
	};

	TimeCardService.prototype.calculateDiff = function(before, after) {
		var diff = 0;
		diff -= before;
		diff += after;
		return diff;
	};

	angular.module('app').service(
			'timeCardService',
			[ 'DateResource', 'TimeCardResource', 'DailyAttendanceResource',
					'$http', '$filter', TimeCardService ]);

})();
