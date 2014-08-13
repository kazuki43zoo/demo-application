<div ng-controller="TimeCardController as timeCardCtrl"
	ng-init="timeCardCtrl.loadTimeCard()" ng-cloak>

	<div class="form-horizontal">
		<div class="form-group">
			<label for="targetMonth" class="control-label col-sm-2 "><spring:message
					code="targetMonth" text="Target Month" /></label>
			<div class="col-sm-3">
				<input type="month" id="targetMonth"
					class="form-control targetMonth" min="2014-01" max="9999-12"
					ng-model="timeCardCtrl.targetMonth"
					ng-change="timeCardCtrl.loadTimeCard()">
			</div>
			<div class="col-sm-4">
				<button class="btn btn-default" ng-click="timeCardCtrl.loadToday()">
					<spring:message code="todayBtn" text="Today" />
				</button>
			</div>
		</div>

		<div class="form-group">
			<label for="defaultWorkPlaceUuid" class="control-label col-sm-2"><spring:message
					code="workPlace" text="Work Place" /></label>
			<div class="col-sm-4">
				<select id="defaultWorkPlaceUuid" class="form-control workPlaceUuid"
					ng-model="timeCardCtrl.timeCard.workPlaceUuid"
					ng-change="timeCardCtrl.calculateTimeCard()">
					<option value=""></option>
					<c:forEach items="${CL_WORK_PLACE}" var="workPlaceEntry">
						<option value="${f:h(workPlaceEntry.key)}">${f:h(workPlaceEntry.value)}</option>
					</c:forEach>
				</select>
			</div>
			<div class="col-sm-4">
				<button class="btn btn-default"
					ng-click="timeCardCtrl.initTimeCard()">
					<spring:message code="initializeBtn" text="Initialize" />
				</button>
				<button class="btn btn-default"
					ng-disabled="
					timeCardCtrl.timeCard.stored === false
					||
					timeCardCtrl.needSaveTimeCard() === false
					"
					ng-click="timeCardCtrl.loadTimeCard()">
					<span class="glyphicon glyphicon-refresh"></span>
					<spring:message code="reloadBtn" text="Reload" />
				</button>
				<button class="btn btn-default"
					ng-disabled="
					timeCardCtrl.timeCard.stored === true
					&&
					timeCardCtrl.needSaveTimeCard() === false
					"
					ng-click="timeCardCtrl.saveTimeCard()">
					<span class="glyphicon glyphicon-floppy-save"></span>
					<spring:message code="saveBtn" text="Save" />
				</button>
			</div>
		</div>
	</div>

	<table class="table table-hover" style="margin-bottom: 0px;">
		<thead>
			<tr>
				<th width="75px"><label for="targetDay"><spring:message
							code="days" text="Days" /></label></th>
				<th width="60px"><label for="beginTime"><spring:message
							code="beginTime" text="Begin Time" />
						<button class="btn btn-default btn-sm"
							ng-click="timeCardCtrl.enter()">
							<span class="glyphicon glyphicon-log-in"></span>
						</button> </label></th>
				<th width="60px"><label for="finishTime"><spring:message
							code="finishTime" text="Finish Time" />
						<button class="btn btn-default btn-sm"
							ng-disabled="timeCardCtrl.editableAttendance.beginTime == null"
							ng-click="timeCardCtrl.exit()">
							<span class="glyphicon glyphicon-log-out"></span>
						</button> </label></th>
				<th width="60px"><label><spring:message
							code="paidLeave" text="Paid Leave" /></label></th>
				<th width="70px"><label for="specialWorkCode"><spring:message
							code="specialWork" text="Special Work" /></label></th>
				<th><label for="note"><spring:message code="note"
							text="Note" /></label></th>
				<th width="150px"><label for="workPlaceUuid"><spring:message
							code="workPlace" text="Work Place" /></label></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><input type="number" id="targetDay" class="form-control"
					min="1" max="{{timeCardCtrl.timeCard.attendances.length}}"
					ng-model="timeCardCtrl.targetDay"
					ng-change="timeCardCtrl.setEditableAttendance()"></td>

				<td><input type="time" id="beginTime"
					class="form-control beginTime" maxlength="5"
					ng-model="timeCardCtrl.editableAttendance.beginTime"
					ng-disabled="timeCardCtrl.editableAttendance.paidLeave === true"
					ng-blur="timeCardCtrl.calculateTime('beginTime')"></td>

				<td><input type="time" id="finishTime"
					class="form-control finishTime" maxlength="5"
					ng-model="timeCardCtrl.editableAttendance.finishTime"
					ng-disabled="
					timeCardCtrl.editableAttendance.beginTime == null
					||
					timeCardCtrl.editableAttendance.paidLeave === true
					"
					ng-blur="timeCardCtrl.calculateTime('finishTime')"></td>

				<td><input type="checkbox" id="paidLeave" class="paidLeave"
					value="true" ng-model="timeCardCtrl.editableAttendance.paidLeave"
					ng-disabled="
					timeCardCtrl.timeCardService.isHoliday(timeCardCtrl.attendance.targetDate)
					||
					(
					timeCardCtrl.editableAttendance.paidLeave === false
					&&
					timeCardCtrl.editableAttendance.absence === false
					)
					"
					ng-change="timeCardCtrl.changePaidLeave()"></td>

				<td><select id="specialWorkCode"
					class="form-control specialWorkCode"
					ng-model="timeCardCtrl.editableAttendance.specialWorkCode"
					ng-disabled="
					timeCardCtrl.editableAttendance.paidLeave === true
					||
					(
					timeCardCtrl.editableAttendance.tardyOrEarlyLeaving === false
					&&
					timeCardCtrl.editableAttendance.absence === false
					)
					||
					timeCardCtrl.timeCardService.isHoliday(timeCardCtrl.editableAttendance.targetDate)
					">
						<option value=""></option>
						<c:forEach items="${CL_ATTENDANCE_SPECIAL_WORK}"
							var="specialWorkEntry">
							<option value="${f:h(specialWorkEntry.key)}">${f:h(specialWorkEntry.value)}</option>
						</c:forEach>
				</select></td>

				<td><textarea id="note" class="form-control note"
						ng-model="timeCardCtrl.editableAttendance.note" rows="1"></textarea></td>

				<td><select id="workPlaceUuid"
					class="form-control workPlaceUuid"
					ng-model="timeCardCtrl.editableAttendance.workPlaceUuid"
					ng-disabled="timeCardCtrl.editableAttendance.paidLeave === true"
					ng-change="timeCardCtrl.calculateTime('workPlaceUuid')">
						<option value=""></option>
						<c:forEach items="${CL_WORK_PLACE}" var="workPlaceEntry">
							<option value="${f:h(workPlaceEntry.key)}">${f:h(workPlaceEntry.value)}</option>
						</c:forEach>
				</select></td>

			</tr>
		</tbody>
	</table>

	<table class="table table-hover" style="margin-top: 0px;">
		<thead>
			<tr>
				<th width="75px"><spring:message code="actualWorkTime"
						text="Actual Working Time" /></th>
				<td class="center">{{timeCardCtrl.editableAttendance.actualWorkingMinute|formatTime}}</td>

				<th width="60px"><spring:message code="compensationTime"
						text="Compensation Time" /></th>
				<td class="center">{{timeCardCtrl.editableAttendance.compensationMinute|formatTime}}</td>

				<th width="60px"><spring:message code="midnightWorkingTime"
						text="Midnight Working Time" /></th>
				<td class="center">{{timeCardCtrl.editableAttendance.midnightWorkingMinute|formatTime}}</td>

				<th width="60px"><spring:message code="tardyOrEarlyLeaving"
						text="Tardy Or Early Leaving" /></th>
				<td class="check" ng-switch
					on="timeCardCtrl.editableAttendance.tardyOrEarlyLeaving"><span
					class="glyphicon glyphicon-ok" ng-switch-when="true"></span><span
					ng-switch-default>-</span></td>

				<th width="70px"><spring:message code="absence" text="Absence" /></th>
				<td class="check" ng-switch
					on="timeCardCtrl.editableAttendance.absence"><span
					class="glyphicon glyphicon-ok" ng-switch-when="true"></span><span
					ng-switch-default>-</span></td>

				<td class="center">
					<button class="btn btn-default"
						ng-disabled="
						timeCardCtrl.timeCard.stored === false
						||
						timeCardCtrl.needSaveEditableAttendance() === false
						"
						ng-click="timeCardCtrl.saveDailyAttendance()">
						<span class="glyphicon glyphicon-floppy-save"></span>
						<spring:message code="saveBtn" text="Save" />
					</button>
				</td>
			</tr>
		</thead>
	</table>

	<table class="table table-hover listTable">
		<thead>
			<tr>
				<th width="30px" title="<spring:message code="days" text="Days" />"><spring:message
						code="thead.days" text="D" /></th>
				<th width="30px"
					title="<spring:message code="dayOfWeek" text="Day Of Week" />"><spring:message
						code="thead.dayOfWeek" text="W" /></th>
				<th width="55px"
					title="<spring:message code="beginTime" text="Begin Time" />"><spring:message
						code="thead.beginTime" text="B Time" /></th>
				<th width="55px"
					title="<spring:message code="finishTime" text="Finish Time" />"><spring:message
						code="thead.finishTime" text="F Time" /></th>
				<th width="60px"
					title="<spring:message code="actualWorkTime" text="Actual Work Time" />"><spring:message
						code="thead.actualWorkTime" text="A W Time" /></th>
				<th width="50px"
					title="<spring:message code="compensationTime" text="Compensation Time" />"><spring:message
						code="thead.compensationTime" text="C Time" /></th>
				<th width="50px"
					title="<spring:message code="midnightWorkingTime" text="Midnight Working Time" />"><spring:message
						code="thead.midnightWorkingTime" text="M Time" /></th>
				<th width="30px"
					title="<spring:message code="tardyOrEarlyLeaving" text="Tardy Or Early Leaving" />"><spring:message
						code="thead.tardyOrEarlyLeaving" text="T E" /></th>
				<th width="30px"
					title="<spring:message code="absence" text="Absence" />"><spring:message
						code="thead.absence" text="A" /></th>
				<th width="30px"
					title="<spring:message code="paidLeave" text="Paid Leave" />"><spring:message
						code="thead.paidLeave" text="P L" /></th>
				<th width="30px"
					title="<spring:message code="specialWork" text="Special Work" />"><spring:message
						code="thead.specialWork" text="S W" /></th>
				<th title="<spring:message code="note" text="Note" />"><spring:message
						code="thead.note" text="Note" /></th>
				<th width="130px"
					title="<spring:message code="workPlace" text="Work Place" />"><spring:message
						code="thead.workPlace" text="Work Place" /></th>
			</tr>
		</thead>

		<tbody>
			<tr ng-repeat="attendance in timeCardCtrl.timeCard.attendances"
				ng-click="
				timeCardCtrl.targetDay=$index+1;
				timeCardCtrl.setEditableAttendance()"
				ng-class="{selected:attendance.targetDate==timeCardCtrl.editableAttendance.targetDate}">

				<td class="codeName {{attendance.targetDate|date:'EEE'}}">{{attendance.targetDate|date:'d'}}</td>
				<td class="codeName {{attendance.targetDate|date:'EEE'}}">{{attendance.targetDate|date:'EEE'}}</td>
				<td class="time">{{attendance.beginTime}}</td>
				<td class="time">{{attendance.finishTime}}</td>
				<td class="time">{{attendance.actualWorkingMinute|formatTime:true}}</td>
				<td class="time">{{attendance.compensationMinute|formatTime:true}}</td>
				<td class="time">{{attendance.midnightWorkingMinute|formatTime:true}}</td>
				<td class="check"><span class="glyphicon glyphicon-ok"
					ng-show="attendance.tardyOrEarlyLeaving === true"></span></td>
				<td class="check"><span class="glyphicon glyphicon-ok"
					ng-show="attendance.absence === true"></span></td>
				<td class="check"><span class="glyphicon glyphicon-ok"
					ng-show="attendance.paidLeave === true"></span></td>
				<td class="codeName"><c:forEach var="specialWorkEntry"
						items="${CL_ATTENDANCE_SPECIAL_WORK}">
						<span
							ng-show="attendance.specialWorkCode === '${f:h(specialWorkEntry.key)}'">${f:h(specialWorkEntry.value)}</span>
					</c:forEach></td>
				<td class="text">{{attendance.note}}</td>
				<td><c:forEach var="workPlaceEntry" items="${CL_WORK_PLACE}">
						<span class="text"
							ng-show="attendance.workPlaceUuid === '${f:h(workPlaceEntry.key)}'">${f:h(workPlaceEntry.value)}</span>
					</c:forEach></td>

			</tr>
		</tbody>
		<tfoot>
			<tr>
				<th colspan="4"><spring:message code="total" text="Total" /></th>
				<td class="time">{{timeCardCtrl.total.actualWorkingMinute|formatTime}}</td>
				<td class="time">{{timeCardCtrl.total.compensationMinute|formatTime}}</td>
				<td class="time">{{timeCardCtrl.total.midnightWorkingMinute|formatTime}}</td>
				<td class="number">{{timeCardCtrl.total.tardyOrEarlyLeavingCount}}</td>
				<td class="number">{{timeCardCtrl.total.absenceCount}}</td>
				<td class="number lastTd">{{timeCardCtrl.total.paidLeaveCount}}</td>
			</tr>
		</tfoot>
	</table>

</div>
