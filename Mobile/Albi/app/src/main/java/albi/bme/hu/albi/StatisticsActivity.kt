package albi.bme.hu.albi

import albi.bme.hu.albi.statistics.AChartEngine
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import org.achartengine.ChartFactory

class StatisticsActivity : AppCompatActivity() {

    private val mAChartEngine = AChartEngine()
    private lateinit var chartLayout: LinearLayout
    private var data = HashMap<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics2)

        chartLayout = findViewById(R.id.chart)

        val intent = intent
        data = intent.getSerializableExtra("statisticData") as (HashMap<String, Int>)
    }

    override fun onResume() {
        super.onResume()
        if(mAChartEngine.mChart == null){
            mAChartEngine.dataToChart = data
            mAChartEngine.initChart()
        }
        /**
         * https://github.com/Backelite/AChartEngine/blob/master/achartengine/src/org/achartengine/ChartFactory.java
         * https://stackoverflow.com/questions/48805646/android-studio-3-x-update-and-lost-connection-with-achartengine-cant-find-symb
         * https://www.survivingwithandroid.com/2014/06/android-chart-tutorial-achartengine.html
         */
        mAChartEngine.mChart = ChartFactory.getLineChartView(this, mAChartEngine.mDataSet, mAChartEngine.mRenderer)
        chartLayout.addView(mAChartEngine.mChart)
    }
}
