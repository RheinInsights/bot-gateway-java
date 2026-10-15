// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.recognizers.text.expression;

import com.microsoft.recognizers.text.expression.english.TimexRelativeConvertEnglish;

import java.time.LocalDateTime;

public class TimexRelativeConvert {
    public static String convertTimexToStringRelative(TimexProperty timex, LocalDateTime referenceDate) {
        return TimexRelativeConvertEnglish.convertTimexToStringRelative(timex, referenceDate);
    }
}
