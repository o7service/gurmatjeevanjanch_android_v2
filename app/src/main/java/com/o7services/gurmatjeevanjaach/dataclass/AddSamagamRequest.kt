package com.o7services.gurmatjeevanjaach.dataclass

import com.google.gson.annotations.SerializedName

data class AddSamagamRequest(
     var organizerName : String? = null,
     var address       : String? = null,
     var details       : String? = null,
     var mapLink       : String? = null,
     var phone         : String? = null,
     var email         : String? = null,
     var startDate     : String? = null,
     var endDate       : String? = null,

)