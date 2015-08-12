package com.hypodiabetic.happ;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;

import com.hypodiabetic.happ.code.nightwatch.Bg;
import com.hypodiabetic.happ.code.nightwatch.BgGraphBuilder;
import com.hypodiabetic.happ.code.nightwatch.Constants;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

/**
 * Created by tim on 12/08/2015.
 */

//Extends the NightWatch BgGraphBuilder class to add additional lines
public class ExtendedGraphBuilder extends BgGraphBuilder  {

    protected ExtendedGraphBuilder(Context context){
        super(context);
    }



    private final List<historicalIOBCOB> iobReadings = historicalIOBCOB.latestForGraph(numValues, start_time * fuzz);    //IOB Line
    private List<PointValue> iobValues = new ArrayList<PointValue>();                                        //IOB line

    @Override
    public LineChartData lineData() {
        LineChartData lineData = new LineChartData(defaultLines());
        lineData.setAxisYLeft(yAxis());
        lineData.setAxisXBottom(xAxis());
        return lineData;
    }

    @Override
    public List<Line> defaultLines() {
        addBgReadingValues();
        addIOBValues();                                                                             //IOB line
        List<Line> lines = new ArrayList<Line>();
        lines.add(minShowLine());
        lines.add(maxShowLine());
        lines.add(highLine());
        lines.add(lowLine());
        lines.add(inRangeValuesLine());
        lines.add(lowValuesLine());
        lines.add(highValuesLine());
        lines.add(iobValuesLine());                                                                 //IOB line
        return lines;
    }

    public Line iobValuesLine(){                                                                    //IOB line
        Line iobValuesLine = new Line(iobValues);
        iobValuesLine.setColor(ChartUtils.COLOR_GREEN);
        iobValuesLine.setHasLines(false);
        iobValuesLine.setPointRadius(3);
        iobValuesLine.setHasPoints(true);
        return iobValuesLine;
    }

    public void addIOBValues(){                                                                     // IOB Line
        for (historicalIOBCOB iobReading : iobReadings) {
            iobValues.add(new PointValue((float) (iobReading.datetime/fuzz), (float) fitRange(iobReading.value)));
        }
    }
    public double fitRange(double value){                                                           //IOB Line
        Double yBgMax = highMark;
        Double yBgMin = lowMark;

        Double yIOBMax = 30D;
        Double yIOBMin = 0.5D;

        Double percent = (value - yIOBMin) / (yIOBMax - yIOBMin);
        return percent * (yBgMax - yBgMin) + yBgMin;
    }

}
