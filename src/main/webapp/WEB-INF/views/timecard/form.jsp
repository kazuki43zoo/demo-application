<div data-ng-controller="TimeCardController as timeCardCtrl" data-ng-init="timeCardCtrl.loadTimeCard()"
    data-ng-cloak>

    <div id="timeCardForm" class="form-horizontal">
        <div class="form-group">
            <label for="targetMonth" class="control-label col-sm-2"><spring:message
                    code="targetMonth" /></label>
            <div class="col-sm-3">
                <input type="month" id="targetMonth" class="form-control targetMonth" max="9999-12"
                    placeholder="yyyy-MM" data-ng-model="timeCardCtrl.targetMonth"
                    data-ng-change="timeCardCtrl.loadTimeCard()">
            </div>
            <div class="col-sm-offset-1 col-sm-6">
                <button class="btn btn-default" data-toggle="modal"
                    data-target="#attendanceEditModal" data-ng-disabled="timeCardCtrl.loading===true"
                    data-ng-click="timeCardCtrl.loadToday()">
                    <spring:message code="todayBtn" />
                </button>
            </div>
        </div>

        <div class="form-group">
            <label for="defaultWorkPlace" class="control-label col-sm-2"><spring:message
                    code="workPlace" /></label>
            <div class="col-sm-4">
                <select id="defaultWorkPlace" class="form-control workPlace"
                    data-ng-model="timeCardCtrl.timeCard.workPlaceUuid"
                    data-ng-change="timeCardCtrl.calculateTimeCard()">
                    <option value=""></option>
                    <c:forEach items="${CL_WORK_PLACE}" var="workPlaceEntry">
                        <option value="${f:h(workPlaceEntry.key)}">
                            ${f:h(workPlaceEntry.value)}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-sm-6">
                <button id="initializeBtn" class="btn btn-default"
                    data-ng-disabled="timeCardCtrl.loading===true" data-ng-click="timeCardCtrl.initTimeCard()">
                    <spring:message code="initializeBtn" />
                </button>
                <button id="reloadBtn" class="btn btn-default"
                    data-ng-disabled="
                            timeCardCtrl.loading===true
                            ||
                            timeCardCtrl.stored === false
                            ||
                            timeCardCtrl.needSaveTimeCard() === false
                            "
                    data-ng-click="timeCardCtrl.loadTimeCard()">
                    <span class="glyphicon glyphicon-refresh"></span>
                    <spring:message code="reloadBtn" />
                </button>
                <button id="saveBtn" class="btn btn-default"
                    data-ng-disabled="
                            timeCardCtrl.loading===true
                            ||
                            (timeCardCtrl.stored === true
                             &&
                             timeCardCtrl.needSaveTimeCard() === false)
                            "
                    data-ng-click="timeCardCtrl.saveTimeCard()">
                    <span class="glyphicon glyphicon-floppy-save"></span>
                    <spring:message code="saveBtn" />
                </button>
            </div>
        </div>
    </div>

    <table id="attendancesTable" class="table table-hover">
        <thead>
            <tr id="attendancesHeader">
                <th class="width-sm" title="<spring:message code="days" />"><spring:message
                        code="thead.days" /></th>
                <th class="width-sm" title="<spring:message code="dayOfWeek" />"><spring:message
                        code="thead.dayOfWeek" /></th>
                <th class="width-time" title="<spring:message code="beginTime" />"><spring:message
                        code="thead.beginTime" /></th>
                <th class="width-time" title="<spring:message code="finishTime" />"><spring:message
                        code="thead.finishTime" /></th>
                <th class="width-time" title="<spring:message code="actualWorkTime" />"><spring:message
                        code="thead.actualWorkTime" /></th>
                <th class="width-time" title="<spring:message code="compensationTime" />"><spring:message
                        code="thead.compensationTime" /></th>
                <th class="width-time" title="<spring:message code="midnightWorkingTime" />"><spring:message
                        code="thead.midnightWorkingTime" /></th>
                <th class="width-sm" title="<spring:message code="tardyOrEarlyLeaving" />"><spring:message
                        code="thead.tardyOrEarlyLeaving" /></th>
                <th class="width-sm" title="<spring:message code="absence" />"><spring:message
                        code="thead.absence" /></th>
                <th class="width-sm" title="<spring:message code="paidLeave" />"><spring:message
                        code="thead.paidLeave" /></th>
                <th class="width-sm" title="<spring:message code="specialWork" />"><spring:message
                        code="thead.specialWork" /></th>
                <th title="<spring:message code="note" />"><spring:message code="thead.note" /></th>
                <th class="width-workPlace" title="<spring:message code="workPlace" />"><spring:message
                        code="thead.workPlace" /></th>
            </tr>
        </thead>

        <tbody data-toggle="modal" data-target="#attendanceEditModal">
            <tr id="attendancesRow{{$index+1}}"
                class="dayOfWeek{{attendance.targetDate | dayOfWeek}}"
                data-ng-class="{holiday : attendance.holiday}"
                data-ng-repeat="attendance in timeCardCtrl.timeCard.attendances"
                data-ng-click=" 
                    timeCardCtrl.targetDay=$index+1;
                    timeCardCtrl.setEditableAttendance();
                    ">
                <td class="codeName day">{{attendance.targetDate | date:'d'}}</td>
                <td class="codeName dayOfWeek">{{attendance.targetDate | date:'EEE'}}</td>
                <td class="time">{{attendance.beginTime}}</td>
                <td class="time">{{attendance.finishTime}}</td>
                <td class="time">{{attendance.actualWorkingMinute | formatTime:true}}</td>
                <td class="time">{{attendance.compensationMinute | formatTime:true}}</td>
                <td class="time">{{attendance.midnightWorkingMinute | formatTime:true}}</td>
                <td class="check"><span class="glyphicon glyphicon-ok"
                    data-ng-show="attendance.tardyOrEarlyLeaving === true"></span></td>
                <td class="check"><span class="glyphicon glyphicon-ok"
                    data-ng-show="attendance.absence === true"></span></td>
                <td class="check"><span class="glyphicon glyphicon-ok"
                    data-ng-show="attendance.paidLeave === true"></span></td>
                <td class="codeName"><c:forEach var="specialWorkEntry"
                        items="${CL_ATTENDANCE_SPECIAL_WORK_DISP}">
                        <span title="${f:h(CL_ATTENDANCE_SPECIAL_WORK[specialWorkEntry.key])}"
                            data-ng-show="attendance.specialWorkCode === '${f:h(specialWorkEntry.key)}'">${f:h(specialWorkEntry.value)}</span>
                    </c:forEach></td>
                <td class="text">{{attendance.note}}</td>
                <td><c:forEach var="workPlaceEntry" items="${CL_WORK_PLACE}">
                        <span class="text"
                            data-ng-show="attendance.workPlaceUuid === '${f:h(workPlaceEntry.key)}'">${f:h(workPlaceEntry.value)}</span>
                    </c:forEach></td>
            </tr>
        </tbody>
        <tfoot>
            <tr id="attendancesFooter" class="total">
                <th colspan="4"><spring:message code="total" /></th>
                <td class="time">{{timeCardCtrl.total.actualWorkingMinute | formatTime}}</td>
                <td class="time">{{timeCardCtrl.total.compensationMinute | formatTime}}</td>
                <td class="time">{{timeCardCtrl.total.midnightWorkingMinute | formatTime}}</td>
                <td class="number">{{timeCardCtrl.total.tardyOrEarlyLeavingCount}}</td>
                <td class="number">{{timeCardCtrl.total.absenceCount}}</td>
                <td class="number lastTd">{{timeCardCtrl.total.paidLeaveCount}}</td>
            </tr>
        </tfoot>
    </table>

    <table id="daySummaryTable" class="table">
        <tbody>
            <tr id="daySummaryRow1">
                <th><spring:message code="workingDays" /></th>
                <td class="number">{{timeCardCtrl.total.baseWorkDays}} <span class="daysText"><spring:message
                            code="days" /></span></td>
                <th><spring:message code="tardyOrEarlyLeavingDays" /></th>
                <td class="number">{{timeCardCtrl.total.tardyOrEarlyLeavingCount}} <span
                    class="daysText"><spring:message code="days" /></span>
                </td>
                <th><spring:message code="tardyOrEarlyLeavingPenaltyTargetDays" /></th>
                <td class="number">{{timeCardCtrl.total.tardyOrEarlyLeavingPenaltyCount}} <span
                    class="daysText"><spring:message code="days" /></span>
                </td>
            </tr>
            <tr id="daySummaryRow2">
                <th><spring:message code="absenceDays" /></th>
                <td class="number">{{timeCardCtrl.total.absenceCount}} <span class="daysText"><spring:message
                            code="days" /></span></td>
                <th><spring:message code="paidLeaveDays" /></th>
                <td class="number">{{timeCardCtrl.total.paidLeaveCount}} <span class="daysText"><spring:message
                            code="days" /></span></td>
                <th></th>
                <td class="number"></td>
            </tr>
        </tbody>
    </table>

    <div class="row">
        <div class="col-md-5">
            <table id="timeSummaryTable" class="table">
                <tbody>
                    <tr class="total">
                        <th><spring:message code="baseWorkingTime" /></th>
                        <td><div class="time summary">
                                {{timeCardCtrl.total.baseWorkingMinute | formatTime}}</div></td>
                    </tr>
                    <tr class="total">
                        <th>
                            <div>
                                <spring:message code="workingTime" />
                            </div>
                            <div class="breakdown">
                                <spring:message code="actualWorkTime" />
                            </div>
                            <div class="breakdown">
                                <spring:message code="compensationTime" />
                            </div>
                        </th>
                        <td>
                            <div class="time summary">{{timeCardCtrl.total.workingMinute |
                                formatTime}}</div>
                            <div class="time breakdown">
                                {{timeCardCtrl.total.actualWorkingMinute | formatTime}}</div>
                            <div class="time breakdown">
                                {{timeCardCtrl.total.compensationMinute | formatTime}}</div>
                        </td>
                    </tr>
                    <tr class="total">
                        <th>
                            <div>
                                <spring:message code="liquidationTime" />
                            </div>
                            <div class="breakdown">
                                <spring:message code="overtimeTime" />
                            </div>
                            <div class="breakdown">
                                <spring:message code="penaltyTime" />
                            </div>
                        </th>
                        <td>
                            <div class="time summary"
                                data-ng-class="{minus:timeCardCtrl.total.liquidationMinute < 0}">
                                {{timeCardCtrl.total.liquidationMinute | formatTime}}</div>
                            <div class="time breakdown"
                                data-ng-class="{minus:timeCardCtrl.total.overtimeTime < 0}">
                                {{timeCardCtrl.total.overtimeTime | formatTime}}</div>
                            <div class="time breakdown"
                                data-ng-class="{minus:timeCardCtrl.total.penaltyTime < 0}">
                                {{timeCardCtrl.total.penaltyTime | formatTime}}</div>
                        </td>
                    </tr>
                    <tr class="total">
                        <th><spring:message code="midnightWorkingTime" /></th>
                        <td><div class="time summary">
                                {{timeCardCtrl.total.midnightWorkingMinute | formatTime}}</div></td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="col-md-7">
            <div class="noteArea">
                <table class="table">
                    <thead>
                        <tr>
                            <th colspan="6"><spring:message code="note" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td colspan="6"><textarea id="timeCardNote" class="form-control"
                                    rows="5" data-ng-model="timeCardCtrl.timeCard.note"></textarea></td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <!--  
            <table>
                <thead>
                    <tr>
                        <th colspan="6">勤務体系(デフォルト勤務先)</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th>規定勤務時間</th>
                        <td>{{timeCardCtrl.defaultWorkPlace.baseBeginTime}} -
                            {{timeCardCtrl.defaultWorkPlace.baseFinishTime}}</td>
                        <th>規定労働時間</th>
                        <td>{{timeCardCtrl.defaultWorkPlace.baseWorkTimeMinute | formatTime}}</td>
                        <th>作業時間単位</th>
                        <td>{{timeCardCtrl.defaultWorkPlace.unitTime}}</td>
                    </tr>
                    <tr height="100px">
                        <td colspan="2">
                            <div ng-repeat="breakTime in timeCardCtrl.defaultWorkPlace.breakTimes">
                                {{breakTime.beginTime}} - {{breakTime.finishTime}}</div>
                        </td>
                        <td colspan="2"></td>
                        <td colspan="2"></td>
                    </tr>
                </tbody>
            </table>
-->
        </div>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="attendanceEditModal" tabindex="-1" role="dialog"
        aria-labelledby="attendanceEditModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" id="attendanceEditModalCloseBtn" class="close"
                        data-dismiss="modal">
                        <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                    </button>
                    <h4
                        class="modal-title dayOfWeek{{timeCardCtrl.editableAttendance.targetDate | dayOfWeek}}"
                        data-ng-class="{holiday : timeCardCtrl.editableAttendance.holiday}"
                        id="attendanceEditModalLabel">
                        <span class="date">{{timeCardCtrl.editableAttendance.targetDate |
                            date:'yyyy/MM/dd (EEE)'}}</span>
                    </h4>
                </div>
                <div class="modal-body">
                    <div id="attendanceInputArea">
                        <table id="attendanceInputTable" class="table">
                            <thead>
                                <tr>
                                    <th class="targetDay"><label for="targetDay"><spring:message
                                                code="targetDays" /></label></th>
                                    <th class="time"><label for="beginTime"><spring:message
                                                code="beginTime" /><br>
                                            <button id="enterBtn" class="btn btn-default btn-xs"
                                                data-ng-disabled="timeCardCtrl.loading === true"
                                                data-ng-click="timeCardCtrl.enter()">
                                                <span class="glyphicon glyphicon-log-in"></span>
                                            </button></label></th>
                                    <th class="time"><label for="finishTime"><spring:message
                                                code="finishTime" /><br>
                                            <button id="exitBtn" class="btn btn-default btn-xs"
                                                data-ng-disabled="
                                                    timeCardCtrl.loading === true
                                                    ||
                                                    timeCardCtrl.editableAttendance.beginTime == null
                                                    "
                                                data-ng-click="timeCardCtrl.exit()">
                                                <span class="glyphicon glyphicon-log-out"></span>
                                            </button></label></th>
                                    <th class="paidLeave"><label for="paidLeave"><spring:message
                                                code="paidLeave" /></label></th>
                                    <th class="specialWork"><label for="specialWork"><spring:message
                                                code="specialWork" /></label></th>
                                    <th><label for="note"><spring:message code="note" /></label></th>
                                    <th class="workPlace"><label for="workPlace"><spring:message
                                                code="workPlace" /></label></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><input type="number" id="targetDay"
                                        class="form-control" required="required" min="1"
                                        max="{{timeCardCtrl.timeCard.attendances.length}}"
                                        data-ng-model="timeCardCtrl.targetDay"
                                        data-ng-disabled="timeCardCtrl.loading === true"
                                        data-ng-change="timeCardCtrl.setEditableAttendance()"></td>
                                    <td><input type="time" id="beginTime" class="form-control"
                                        maxlength="5" placeholder="HH:mm"
                                        data-ng-model="timeCardCtrl.editableAttendance.beginTime"
                                        data-ng-disabled="timeCardCtrl.loading === true"
                                        data-ng-readonly="timeCardCtrl.editableAttendance.paidLeave === true"
                                        data-ng-blur="timeCardCtrl.calculateTime('beginTime')"></td>
                                    <td><input type="time" id="finishTime" class="form-control"
                                        maxlength="5" placeholder="HH:mm"
                                        data-ng-model="timeCardCtrl.editableAttendance.finishTime"
                                        data-ng-disabled="timeCardCtrl.loading === true"
                                        data-ng-readonly="
                                            timeCardCtrl.editableAttendance.paidLeave === true
                                            ||
                                            timeCardCtrl.editableAttendance.beginTime == null
                                            "
                                        data-ng-blur="timeCardCtrl.calculateTime('finishTime')"></td>
                                    <td class="center"><input type="checkbox" id="paidLeave"
                                        value="true"
                                        data-ng-model="timeCardCtrl.editableAttendance.paidLeave"
                                        data-ng-disabled="
                                            timeCardCtrl.loading === true
                                            ||
                                            timeCardCtrl.editableAttendance.holiday === true
                                            ||
                                            (timeCardCtrl.editableAttendance.paidLeave === false
                                             &&
                                             timeCardCtrl.editableAttendance.absence === false)
                                            "
                                        data-ng-change="timeCardCtrl.calculateTime()"></td>
                                    <td><select id="specialWork" class="form-control"
                                        data-ng-model="timeCardCtrl.editableAttendance.specialWorkCode"
                                        data-ng-disabled="
                                            timeCardCtrl.loading === true
                                            ||
                                            timeCardCtrl.editableAttendance.paidLeave === true
                                            ||
                                            timeCardCtrl.editableAttendance.holiday === true
                                            "
                                        data-ng-change="timeCardCtrl.calculateTime()">
                                            <option value=""></option>
                                            <c:forEach items="${CL_ATTENDANCE_SPECIAL_WORK}"
                                                var="specialWorkEntry">
                                                <c:choose>
                                                    <c:when
                                                        test="${fn:startsWith(specialWorkEntry.key,'0')}">
                                                        <option value="${f:h(specialWorkEntry.key)}"
                                                            data-ng-if="
                                                                timeCardCtrl.editableAttendance.beginTime != null
                                                                &&
                                                                timeCardCtrl.editableAttendance.tardyOrEarlyLeaving === true
                                                                ">
                                                            ${f:h(specialWorkEntry.value)}</option>
                                                    </c:when>
                                                    <c:when
                                                        test="${fn:startsWith(specialWorkEntry.key,'1')}">
                                                        <option value="${f:h(specialWorkEntry.key)}"
                                                            data-ng-if="timeCardCtrl.editableAttendance.beginTime == null">
                                                            ${f:h(specialWorkEntry.value)}</option>
                                                    </c:when>
                                                </c:choose>
                                            </c:forEach>
                                    </select></td>
                                    <td><textarea id="note" class="form-control"
                                            data-ng-model="timeCardCtrl.editableAttendance.note" rows="1"></textarea></td>
                                    <td><select id="workPlace" class="form-control"
                                        data-ng-model="timeCardCtrl.editableAttendance.workPlaceUuid"
                                        data-ng-disabled="
                                            timeCardCtrl.loading === true
                                            ||
                                            timeCardCtrl.editableAttendance.paidLeave === true
                                            "
                                        data-ng-change="timeCardCtrl.calculateTime()">
                                            <option value=""></option>
                                            <c:forEach items="${CL_WORK_PLACE}" var="workPlaceEntry">
                                                <option value="${f:h(workPlaceEntry.key)}">
                                                    ${f:h(workPlaceEntry.value)}</option>
                                            </c:forEach>
                                    </select></td>
                                </tr>
                            </tbody>
                        </table>
                        <table id="attendanceInputCalculationResultTable" class="table">
                            <thead>
                                <tr>
                                    <th><spring:message code="actualWorkTime" /></th>
                                    <td class="center">
                                        {{timeCardCtrl.editableAttendance.actualWorkingMinute |
                                        formatTime}}</td>
                                    <th><spring:message code="compensationTime" /></th>
                                    <td class="center">
                                        {{timeCardCtrl.editableAttendance.compensationMinute |
                                        formatTime}}</td>
                                    <th><spring:message code="midnightWorkingTime" /></th>
                                    <td class="center">
                                        {{timeCardCtrl.editableAttendance.midnightWorkingMinute |
                                        formatTime}}</td>
                                    <th><spring:message code="tardyOrEarlyLeaving" /></th>
                                    <td class="check" data-ng-switch
                                        data-on="timeCardCtrl.editableAttendance.tardyOrEarlyLeaving">
                                        <span class="glyphicon glyphicon-ok" data-ng-switch-when="true"></span>
                                        <span data-ng-switch-default>-</span>
                                    </td>
                                    <th><spring:message code="absence" /></th>
                                    <td class="check" data-ng-switch
                                        data-on="timeCardCtrl.editableAttendance.absence"><span
                                        class="glyphicon glyphicon-ok" data-ng-switch-when="true"></span><span
                                        data-ng-switch-default>-</span></td>
                                    <td class="center">
                                        <button id="dailySaveBtn" class="btn btn-default"
                                            data-ng-disabled="
                                                timeCardCtrl.loading === true
                                                ||
                                                timeCardCtrl.stored === false
                                                ||
                                                timeCardCtrl.needSaveEditableAttendance() === false
                                                "
                                            data-ng-click="timeCardCtrl.saveDailyAttendance()">
                                            <span class="glyphicon glyphicon-floppy-save"></span>
                                            <spring:message code="saveBtn" />
                                        </button>
                                    </td>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>