package albi.bme.hu.albi.statistics

import android.annotation.SuppressLint
import android.graphics.Color
import org.achartengine.GraphicalView
import org.achartengine.chart.PointStyle
import org.achartengine.model.XYMultipleSeriesDataset
import org.achartengine.model.XYSeries
import org.achartengine.renderer.XYMultipleSeriesRenderer
import org.achartengine.renderer.XYSeriesRenderer
import java.text.SimpleDateFormat
import kotlin.collections.HashMap

class AChartEngine {

    var mChart: GraphicalView? = null
    var mDataSet: XYMultipleSeriesDataset? = XYMultipleSeriesDataset()
    var mRenderer: XYMultipleSeriesRenderer? = XYMultipleSeriesRenderer()
    var mCurrentSeries: XYSeries? = null
    var mCurrentRenderer: XYSeriesRenderer? = null
    var dataToChart = HashMap<String, Int>()
    @SuppressLint("SimpleDateFormat")
    private var sdf = SimpleDateFormat("YYYY.MM.DD")

    fun initChart(){
        mCurrentSeries = XYSeries("Flat statistics")
        mDataSet!!.addSeries(mCurrentSeries)
        //https://stackoverflow.com/questions/47204146/how-to-iterate-over-hashmap-in-kotlin
        for((key, value) in dataToChart){
            var index: Int = 0
            mCurrentSeries!!.add(index.toDouble(), value.toDouble())
            mRenderer!!.addXTextLabel(value.toDouble(), sdf.format(key))
            index++
        }

        /**
         * when using achartengine is that the dimensions are expressed in pixel not in dp!
         */
        mCurrentRenderer = XYSeriesRenderer()
        mCurrentRenderer!!.lineWidth = 5F
        mCurrentRenderer!!.color = Color.RED
        mCurrentRenderer!!.isDisplayBoundingPoints = true
        mCurrentRenderer!!.pointStyle = PointStyle.CIRCLE
        mCurrentRenderer!!.pointStrokeWidth = 7F
        mRenderer!!.addSeriesRenderer(mCurrentRenderer)

        mRenderer!!.marginsColor = Color.argb(0x00, 0xff, 0x00, 0x00)

        mRenderer!!.setPanEnabled(false, false)
        mRenderer!!.yAxisMax = 7.0
        mRenderer!!.yAxisMin = 0.0
        mRenderer!!.xAxisMin = 0.0
        mRenderer!!.xAxisMax = 7.0
        mRenderer!!.setShowGrid(true)
        mRenderer!!.gridLineWidth = 5F

    }

}