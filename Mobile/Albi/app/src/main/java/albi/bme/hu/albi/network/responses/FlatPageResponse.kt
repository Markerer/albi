package albi.bme.hu.albi.network.responses

import albi.bme.hu.albi.model.Flat

class FlatPageResponse {
    var docs: List<Flat>? = null

    var total: Int? = null
    var limit: Int? = null
    var page: String? = null
    var pages: Int? = null
}