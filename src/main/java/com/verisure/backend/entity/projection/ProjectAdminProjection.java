package com.verisure.backend.entity.projection;

import com.verisure.backend.entity.Project;

public interface ProjectAdminProjection {
    
    Project getProject();
    Long getFavCount();
    Long getAppCount();

}