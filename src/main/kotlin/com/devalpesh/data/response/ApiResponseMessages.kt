package com.devalpesh.data.response

import com.devalpesh.util.Constant

object ApiResponseMessages {
    const val USER_ALREADY_EXIST = "A user with this email already exist."
    const val FIELDS_BLANK = "Fields may not be empty."
    const val INVALID_CREDENTIALS = "Oops!. that is not correct, please try again."
    const val USER_NOT_FOUND = "The user couldn't be found."
    const val COMMENT_TOO_LONG = "Comment length must not exceed ${Constant.MAX_COMMENT_LENGTH} characters."
}