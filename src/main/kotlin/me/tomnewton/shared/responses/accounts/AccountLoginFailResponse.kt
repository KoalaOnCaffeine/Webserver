package me.tomnewton.shared.responses.accounts

import me.tomnewton.shared.responses.ErrorResponse

class AccountLoginFailResponse(reason: String) : ErrorResponse(ACCOUNT_LOGIN_FAIL, reason) {
}