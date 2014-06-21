package com.github.kazuki43zoo.auth.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;

import com.google.common.net.HttpHeaders;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private static final long serialVersionUID = 1L;

    private final String agent;

    private final String trackingId;

    private final String forwardedFor;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.agent = request.getHeader(HttpHeaders.USER_AGENT);
        this.trackingId = (String) request.getAttribute("X-Track");
        this.forwardedFor = request.getHeader(HttpHeaders.X_FORWARDED_FOR);
    }

    @Override
    public String getRemoteAddress() {
        if (StringUtils.hasLength(forwardedFor)) {
            return forwardedFor;
        } else {
            return super.getRemoteAddress();
        }
    }

    public String getAgent() {
        return agent;
    }

    public String getTrackingId() {
        return trackingId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((agent == null) ? 0 : agent.hashCode());
        result = prime * result + ((forwardedFor == null) ? 0 : forwardedFor.hashCode());
        result = prime * result + ((trackingId == null) ? 0 : trackingId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        CustomWebAuthenticationDetails other = (CustomWebAuthenticationDetails) obj;
        if (agent == null) {
            if (other.agent != null)
                return false;
        } else if (!agent.equals(other.agent))
            return false;
        if (forwardedFor == null) {
            if (other.forwardedFor != null)
                return false;
        } else if (!forwardedFor.equals(other.forwardedFor))
            return false;
        if (trackingId == null) {
            if (other.trackingId != null)
                return false;
        } else if (!trackingId.equals(other.trackingId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CustomWebAuthenticationDetails [agent=" + agent + ", trackingId=" + trackingId
                + ", forwardedFor=" + forwardedFor + ", toString()=" + super.toString() + "]";
    }

}
