package albi.bme.hu.albi.statistics

import android.graphics.Color
import org.achartengine.GraphicalView
import org.achartengine.chart.PointStyle
import org.achartengine.model.XYMultipleSeriesDataset
import org.achartengine.model.XYSeries
import org.achartengine.renderer.XYMultipleSeriesRenderer
import org.achartengine.renderer.XYSeriesRenderer
import java.util.*

class AChartEngine {

    var mChart: GraphicalView? = null
    var mDataSet: XYMultipleSeriesDataset? = XYMultipleSeriesDataset()
    var mRenderer: XYMultipleSeriesRenderer? = XYMultipleSeriesRenderer()
    var mCurrentSeries: XYSeries? = null
    var mCurrentRenderer: XYSeriesRenderer? = null

    fun initChart(){
        mCurrentSeries = XYSeries("Flat statistics")
        mDataSet!!.addSeries(mCurrentSeries)
        for(i in 1..7){
            mCurrentSeries!!.add(i.toDouble(), Math.abs(Random().nextInt().toDouble()) % 7)
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

    fun setData(){

    }

}