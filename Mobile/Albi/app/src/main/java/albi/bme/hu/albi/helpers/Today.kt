package albi.bme.hu.albi.helpers

import java.util.*

class Today {
    init{
        setToday()
    }
    private fun setToday(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        date = "$year.$month.$day"
    }

    private var date: String? = null

}