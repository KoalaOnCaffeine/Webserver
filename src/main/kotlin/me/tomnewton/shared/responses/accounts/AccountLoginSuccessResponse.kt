package me.tomnewton.shared.responses.accounts

import me.tomnewton.shared.responses.Response

class AccountLoginSuccessResponse(token: String) : Response(ACCOUNT_LOGIN_SUCCESS, "Successfully logged in", "{token: \"$token\"}")