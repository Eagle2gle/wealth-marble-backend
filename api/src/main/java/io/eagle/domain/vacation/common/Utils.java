package io.eagle.domain.vacation.common;

import io.eagle.domain.vacation.dto.request.OptionalUserIdDto;

public final class Utils {
    public static Long getUserId(OptionalUserIdDto user){
        if(user == null){
            return null;
        } else{
            return user.getUserId();
        }
    }
}
