package com.smartpaper.utils

import com.smartpaper.domains.screen.Screen
import com.smartpaper.domains.user.User
/**
 * Created by IntelliJ IDEA.
 * User: vbarrier
 * Date: 10/10/11
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */
class ConstantUtils {

    static accountTypes = [
        (User.COMPANY_ACCOUNT): 'account.type.company',
        (User.PERSONAL_ACCOUNT): 'account.type.personal',
    ]
}
