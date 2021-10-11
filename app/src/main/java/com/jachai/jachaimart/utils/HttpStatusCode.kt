package com.jachai.jachaimart.utils

object HttpStatusCode {
    // 2xx Success
    const val HTTP_OK = 200
    const val HTTP_CREATED = 201
    const val HTTP_ACCEPTED = 202
    const val HTTP_NO_CONTENT = 204
    const val HTTP_RESET_CONTENT = 205
    const val HTTP_PARTIAL_CONTENT = 206

    // 3xx Redirection
    const val HTTP_MULTIPLE_CHOICES = 300
    const val HTTP_MOVED_PERMANENTLY = 301
    const val HTTP_FOUND = 302
    const val HTTP_SEE_OTHER = 303
    const val HTTP_NOT_MODIFIED = 304
    const val HTTP_USE_PROXY = 305
    const val HTTP_SWITCH_PROXY = 306
    const val HTTP_TEMPORARY_REDIRECT = 307
    const val HTTP_PERMANENT_REDIRECT = 308

    // 4xx Client Errors
    const val HTTP_BAD_REQUEST = 400
    const val HTTP_UNAUTHORIZED = 401
    const val HTTP_FORBIDDEN = 403
    const val HTTP_NOT_FOUND = 404
    const val HTTP_METHOD_NOT_ALLOWED = 405
    const val HTTP_NOT_ACCEPTABLE = 406
    const val HTTP_PROXY_AUTHENTICATION_REQUIRED = 407
    const val HTTP_REQUEST_TIME_OUT = 408
    const val HTTP_CONFLICT = 409
    const val HTTP_GONE = 410
    const val HTTP_LENGTH_REQUIRED = 411
    const val HTTP_PRECONDITION_FAILED = 412

    const val HTTP_INTERNAL_SERVER_ERROR = 500

}