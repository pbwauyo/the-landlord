package com.peter.thelandlord.domain.models

data class Rental (var rentalID: String = "", var monthlyAmount: String = "", var tenantName: String = "",
                var tenantContact: String = "", var tenancyStartDate: String = "", var propertyID: String = "")