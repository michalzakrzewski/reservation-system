package com.zakrzewski.reservationsystem.exceptions.response;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record ErrorResponse (String message){
}
