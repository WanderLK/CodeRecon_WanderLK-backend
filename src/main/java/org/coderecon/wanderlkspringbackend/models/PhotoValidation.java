package org.coderecon.wanderlkspringbackend.models;

import lombok.Data;

@Data
public class PhotoValidation {
    private Boolean aspectRatio;
    private Boolean adjustedSize;
    private Boolean resolution;
    private Boolean recognize;
    private Boolean orientation;
}
