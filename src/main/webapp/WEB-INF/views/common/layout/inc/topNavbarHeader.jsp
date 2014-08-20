<a id="homeLink" class="navbar-brand" href="${contextPath}/"><span
    class="glyphicon glyphicon-home"></span> Demo Application</a>
<span id="clock" class="navbar-brand">{{navBarCtrl.currentDateTime | date }}
    {{navBarCtrl.currentDateTime | date:'(EEE) HH:mm:ss' }}</span>
