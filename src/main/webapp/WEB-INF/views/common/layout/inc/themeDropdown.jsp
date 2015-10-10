<ul class="nav navbar-nav">
    <li class="dropdown">
        <a id="themeSwitchLink"
           href="javascript:void(0);"
           class="dropdown-toggle"
           data-toggle="dropdown">
            <spring:message code="theme"/>
            <b class="caret"></b>
        </a>
        <ul class="dropdown-menu">
            <li>
                <a id="defaultThemeLink" href="?theme=default">
                    <spring:message code="theme.default"/>
                </a>
            </li>
            <li>
                <a id="blueThemeLink" href="?theme=blue">
                    <spring:message code="theme.blue"/>
                </a>
            </li>
        </ul>
    </li>
</ul>
