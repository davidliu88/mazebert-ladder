package com.mazebert.builders;

import com.mazebert.error.Error;
import com.mazebert.error.Type;
import org.jusecase.builders.Builder;

public class ErrorBuilder implements Builder<Error> {
    private Error error;

    public ErrorBuilder(Error error) {
        this.error = error;
    }

    public Error build() {
        return error;
    }
}
