(function() {
	'use strict';

	function TimeCardController($location, timeCardService) {
		this.$location = $location;
		this.timeCardService = timeCardService;
		this.targetDay = 1;
		this.total = {};
		this.timeCardService.initTotal(this.total);
		this.targetMonth = $location.path().substring(1);
	}

	TimeCardController.prototype.loadTimeCard = function() {
		if (this.targetMonth === '') {
			this.loadToday();
		} else {
			this.$location.path(this.targetMonth);
			this.timeCardService.initTotal(this.total);
			this.timeCard = this.timeCardService.getTimeCard(this.targetMonth,
					this.total);
			var _this = this;
			var reflectLoadResult = function(timeCard) {
				_this.loadedTimeCard = angular.copy(timeCard);
				_this.setEditableAttendance();
			};
			this.timeCard.$promise.then(reflectLoadResult);
		}
	};

	TimeCardController.prototype.loadToday = function() {
		var _this = this;
		var loadToday = function(currentDateTime) {
			var currentDate = new Date(currentDateTime.dateTime);
			var targetMonth = _this.timeCardService.formatDate(currentDate,
					'yyyy-MM');
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
		}
		this.timeCardService.getCurrentDateTime().then(loadToday);
	};

	TimeCardController.prototype.setEditableAttendance = function() {
		var targetDayIndex = this.targetDay - 1;
		this.editableAttendance = this.timeCard.attendances[targetDayIndex];
		this.loadedAttendance = this.loadedTimeCard.attendances[targetDayIndex];
		this.originalAttendance = angular.copy(this.editableAttendance);
	};

	TimeCardController.prototype.needSaveTimeCard = function() {
		return !angular.equals(this.timeCard, this.loadedTimeCard);
	};

	TimeCardController.prototype.needSaveEditableAttendance = function() {
		return !angular.equals(this.editableAttendance, this.loadedAttendance);
	};

	TimeCardController.prototype.initTimeCard = function() {
		this.timeCardService.initTimeCard(this.timeCard, this.total);
	};

	TimeCardController.prototype.saveTimeCard = function() {
		var _this = this;
		var reflectSaveResult = function() {
			angular.copy(_this.timeCard, _this.loadedTimeCard);
			_this.setEditableAttendance();
		}
		this.timeCardService.saveTimeCard(this.targetMonth, this.timeCard)
				.then(reflectSaveResult);
	};

	TimeCardController.prototype.saveDailyAttendance = function() {
		var _this = this;
		var reflectSaveResult = function() {
			angular.copy(_this.editableAttendance, _this.loadedAttendance);
		}
		this.timeCardService.saveDailyAttendance(this.targetMonth,
				this.targetDay, this.editableAttendance)
				.then(reflectSaveResult);
	};

	TimeCardController.prototype.calculateTime = function(fieldName) {
		this.timeCardService.calculateTime(this.editableAttendance,
				this.originalAttendance, this.total, fieldName);
	};

	TimeCardController.prototype.enter = function() {
		var _this = this;
		var enter = function(currentDateTime) {
			var currentDate = new Date(currentDateTime.dateTime);
			var time = _this.timeCardService.formatDate(currentDate, 'HH:mm');
			_this.editableAttendance.beginTime = time;
			_this.calculateTime('beginTime');
			_this.saveDailyAttendance();
		}
		this.timeCardService.getCurrentDateTime().then(enter);
	};

	TimeCardController.prototype.exit = function() {
		var _this = this;
		var exit = function(currentDateTime) {
			var currentDate = new Date(currentDateTime.dateTime);
			var time = _this.timeCardService.formatDate(currentDate, 'HH:mm');
			_this.editableAttendance.finishTime = time;
			_this.calculateTime('finishTime');
			_this.saveDailyAttendance();
		}
		this.timeCardService.getCurrentDateTime().then(exit);
	};

	TimeCardController.prototype.changePaidLeave = function() {
		this.timeCardService.changePaidLeave(this.editableAttendance,
				this.total);
	}

	angular.module('app').controller('TimeCardController',
			[ '$location', 'timeCardService', TimeCardController ]);

})();
