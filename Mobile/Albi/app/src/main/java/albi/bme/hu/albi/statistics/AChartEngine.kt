package albi.bme.hu.albi.statistics

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
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
        mCurrentSeries = XYSeries("")
        mDataSet!!.addSeries(mCurrentSeries)
        //https://stackoverflow.com/questions/47204146/how-to-iterate-over-hashmap-in-kotlin
        var index: Int = 0
        mRenderer!!.xLabels = dataToChart.size
        for((key, value) in dataToChart){
            mCurrentSeries!!.add(index.toDouble(), value.toDouble())
            mRenderer!!.addXTextLabel(index.toDouble(), key)
            index++
        }

        /**
         * when using achartengine is that the dimensions are expressed in pixel not in dp!
         */
        mCurrentRenderer = XYSeriesRenderer()
        mCurrentRenderer!!.lineWidth = 5F
        mCurrentRenderer!!.color = Color.GREEN
        mCurrentRenderer!!.isDisplayBoundingPoints = true
        mCurrentRenderer!!.pointStyle = PointStyle.CIRCLE
        mCurrentRenderer!!.pointStrokeWidth = 10F
        mRenderer!!.addSeriesRenderer(mCurrentRenderer)
        mRenderer!!.marginsColor = Color.argb(0x00, 0xff, 0x00, 0x00)
        mRenderer!!.setPanEnabled(false, false)
        mRenderer!!.yAxisMax = 50.0
        mRenderer!!.yAxisMin = 0.0
        mRenderer!!.setShowGrid(true)
        mRenderer!!.gridLineWidth = 5F
        mRenderer!!.labelsTextSize = 30F
        mRenderer!!.setYLabelsAlign(Paint.Align.RIGHT)
        mRenderer!!.marginsColor = Color.WHITE
        mRenderer!!.xLabelsColor = Color.BLACK
        mRenderer!!.setYLabelsColor(0, Color.BLACK)
        mRenderer!!.xLabelsAngle = 90F
        mRenderer!!.xLabelsAlign = Paint.Align.LEFT
    }

}