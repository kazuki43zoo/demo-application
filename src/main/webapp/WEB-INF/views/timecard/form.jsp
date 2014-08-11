<div ng-controller="TimeCardController as timeCardCtrl"
	ng-init="timeCardCtrl.loadTimeCard()" ng-cloak>

	<div class="form-horizontal">
		<div class="form-group">
			<label for="targetMonth" class="control-label col-sm-2 ">対象月</label>
			<div class="col-sm-3">
				<input type="month" id="targetMonth"
					class="form-control targetMonth"
					ng-model="timeCardCtrl.targetMonth"
					ng-change="timeCardCtrl.loadTimeCard()">
			</div>
			<div class="col-sm-4">
				<button class="btn btn-default" ng-click="timeCardCtrl.loadToday()">今日</button>
			</div>
		</div>

		<div class="form-group">
			<label for="defaultWorkPlaceUuid" class="control-label col-sm-2">勤務先</label>
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
					ng-click="timeCardCtrl.initTimeCard()">初期化</button>
				<button class="btn btn-default"
					ng-disabled="timeCardCtrl.timeCard.stored===false"
					ng-click="timeCardCtrl.loadTimeCard()">
					<span class="glyphicon glyphicon-refresh"></span>再読込
				</button>
				<button class="btn btn-default"
					ng-disabled="
					timeCardCtrl.timeCard.stored===true &&
					timeCardCtrl.needSaveTimeCard()===false"
					ng-click="timeCardCtrl.saveTimeCard()">
					<span class="glyphicon glyphicon-floppy-save"></span>保存
				</button>
			</div>
		</div>
	</div>

	<table class="table table-hover" style="margin-bottom: 0px;">
		<thead>
			<tr>
				<th width="75px"><label for="targetDay">日</label></th>
				<th width="60px"><label for="beginTime">出勤
						<button class="btn btn-default btn-sm"
							ng-click="timeCardCtrl.enter()">
							<span class="glyphicon glyphicon-log-in"></span>
						</button>
				</label></th>
				<th width="60px"><label for="finishTime">退勤
						<button class="btn btn-default btn-sm"
							ng-disabled="timeCardCtrl.editableAttendance.beginTime==null"
							ng-click="timeCardCtrl.exit()">
							<span class="glyphicon glyphicon-log-out"></span>
						</button>
				</label></th>
				<th width="60px"><label>有給</label></th>
				<th width="70px"><label for="specialWorkCode">特殊</label></th>
				<th><label for="note">備考</label></th>
				<th width="150px"><label for="workPlaceUuid">勤務先</label></th>
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
					ng-disabled="timeCardCtrl.editableAttendance.paidLeave===true"
					ng-blur="timeCardCtrl.calculateTime('beginTime')"></td>

				<td><input type="time" id="finishTime"
					class="form-control finishTime" maxlength="5"
					ng-model="timeCardCtrl.editableAttendance.finishTime"
					ng-disabled="timeCardCtrl.editableAttendance.paidLeave===true"
					ng-blur="timeCardCtrl.calculateTime('finishTime')"></td>

				<td><input type="checkbox" id="paidLeave" class="paidLeave"
					value="true" ng-model="timeCardCtrl.editableAttendance.paidLeave"
					ng-disabled="timeCardCtrl.timeCardService.isHoliday(timeCardCtrl.attendance.targetDate)"
					ng-change="timeCardCtrl.changePaidLeave()"></td>

				<td><select id="specialWorkCode"
					class="form-control specialWorkCode"
					ng-model="timeCardCtrl.editableAttendance.specialWorkCode"
					ng-disabled="
					timeCardCtrl.editableAttendance.paidLeave===true||
					(timeCardCtrl.editableAttendance.tardyOrEarlyLeaving===false&&
					timeCardCtrl.editableAttendance.absence===false)||
					timeCardCtrl.timeCardService.isHoliday(timeCardCtrl.editableAttendance.targetDate)">
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
					ng-disabled="timeCardCtrl.editableAttendance.paidLeave===true"
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
				<th width="75px">実働</th>
				<td class="center">{{timeCardCtrl.editableAttendance.actualWorkingMinute|formatTime}}</td>

				<th width="60px">補填</th>
				<td class="center">{{timeCardCtrl.editableAttendance.compensationMinute|formatTime}}</td>

				<th width="60px">深夜</th>
				<td class="center">{{timeCardCtrl.editableAttendance.midnightWorkingMinute|formatTime}}</td>

				<th width="60px">遅早</th>
				<td class="check" ng-switch
					on="timeCardCtrl.editableAttendance.tardyOrEarlyLeaving"><span
					class="glyphicon glyphicon-ok" ng-switch-when="true"></span><span
					ng-switch-default>-</span></td>

				<th width="70px">欠勤</th>
				<td class="check" ng-switch
					on="timeCardCtrl.editableAttendance.absence"><span
					class="glyphicon glyphicon-ok" ng-switch-when="true"></span><span
					ng-switch-default>-</span></td>

				<td class="center">
					<button class="btn btn-default"
						ng-disabled="
						timeCardCtrl.timeCard.stored===false||
						timeCardCtrl.needSaveEditableAttendance()===false"
						ng-click="timeCardCtrl.saveDailyAttendance()">
						<span class="glyphicon glyphicon-floppy-save"></span>保存
					</button>
				</td>
			</tr>
		</thead>
	</table>

	<table class="table table-hover listTable">
		<thead>
			<tr>
				<th width="30px">日</th>
				<th width="30px">曜</th>
				<th width="55px">出勤</th>
				<th width="55px">退勤</th>
				<th width="60px">実働</th>
				<th width="50px">補填</th>
				<th width="50px">深夜</th>
				<th width="30px">遅早</th>
				<th width="30px">欠勤</th>
				<th width="30px">有給</th>
				<th width="30px">特殊</th>
				<th>備考</th>
				<th width="130px">勤務先</th>
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
					ng-show="attendance.tardyOrEarlyLeaving===true"></span></td>
				<td class="check"><span class="glyphicon glyphicon-ok"
					ng-show="attendance.absence===true"></span></td>
				<td class="check"><span class="glyphicon glyphicon-ok"
					ng-show="attendance.paidLeave===true"></span></td>
				<td class="codeName"><c:forEach var="specialWorkEntry"
						items="${CL_ATTENDANCE_SPECIAL_WORK}">
						<span
							ng-show="attendance.specialWorkCode==='${f:h(specialWorkEntry.key)}'">${f:h(specialWorkEntry.value)}</span>
					</c:forEach></td>
				<td class="text">{{attendance.note}}</td>
				<td><c:forEach var="workPlaceEntry" items="${CL_WORK_PLACE}">
						<span class="text"
							ng-show="attendance.workPlaceUuid==='${f:h(workPlaceEntry.key)}'">${f:h(workPlaceEntry.value)}</span>
					</c:forEach></td>

			</tr>
		</tbody>
		<tfoot>
			<tr>
				<th colspan="4">合計</th>
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
