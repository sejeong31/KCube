package com.example.k_kube

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.kcube.Data.Cube
import com.example.kcube.Data.MyTime
import com.example.kcube.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RoomFragment1 : Fragment() {
    var nroom = 0
    var check = 0
    var row = 27
    var column = 3
    val tableIdCode = 1234
    var register = Array(row, { IntArray(column) })
    var map_register = hashMapOf<Int,Int>()
    val tableLayout by lazy { TableLayout(this.context) }
    val tz = TimeZone.getTimeZone("Asia/Seoul")
    val gc = GregorianCalendar(tz)
    var date = gc.get(GregorianCalendar.DAY_OF_MONTH).toString()
    var hour = gc.get(GregorianCalendar.HOUR_OF_DAY).toString()
    var min = gc.get(GregorianCalendar.MINUTE).toString()
    lateinit var cube: ArrayList<Cube>
    lateinit var reserve_room:ArrayList<String>
    lateinit var day:CalendarDay
    var time = arrayListOf<MyTime>()

    lateinit var linearLayout: LinearLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_room_fragment1, null)

        cube = arrayListOf()
        reserve_room =  arrayListOf()
        if(arguments!=null){
            nroom = arguments!!.getInt("roomNum")
            //nroom = 3
            cube = arguments!!.getParcelableArrayList<Cube>("user")!!

            for(i in cube){
                Log.d("프래그먼트1",i.name)
                reserve_room .add(i.name.split(" ")[1])
            }

            day = arguments!!.getParcelable<CalendarDay>("day")!!
        }else nroom = 3
        Log.d("프래그먼트1",nroom.toString())
        linearLayout = view.findViewById(R.id.table1)
        tableLayout.removeAllViews()
        createTable(row, column, view)
        fill_table(view)
        return view
    }

    interface OnReserve1{
        fun onSetReserveTime(time:MyTime)
        fun removeReserveTime(time:MyTime)
        fun setRoomNumber(name:String)
    }

    var Listener:OnReserve1 ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ResourceAsColor")
    fun createTable(rows: Int, cols: Int, view: View) {
        var thour = 0
        var tminute = 0
        for (i in 0 until rows) {
            val row = TableRow(this.context)
            row.layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            for (j in 0 until cols) {
                val textview = TextView(this.context)
                var str: String
                var num = i.toString() + "," + j.toString()
                textview.apply {
                    layoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )
                    textview.setBackgroundResource(R.drawable.border)
                    textview.width = 380
                    textview.height = 110
                    textview.gravity = Gravity.CENTER
                    textview.setBackgroundResource(R.drawable.border)
                    str = "$i$j"
                    textview.setId(str.toInt())
                    Log.d("tvtv1", textview.id.toString())
                    if (i == 0) {
                        if (j == 0) {
                            textview.setBackgroundColor(R.color.myGreen)
                            textview.setTextColor(Color.WHITE)
                            textview.setText("1호실")
                        }
                        if (j == 1) {
                            textview.setBackgroundColor(R.color.myGreen)
                            textview.setTextColor(Color.WHITE)
                            textview.setText("2호실")
                        }
                        if (j == 2) {
                            textview.setBackgroundColor(R.color.myGreen)
                            textview.setTextColor(Color.WHITE)
                            textview.setText("3호실")
                        }
                        textview.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    } else if (i != 0) {
                        if (i % 2 == 0) {
                            textview.text = (9 + i / 2 - 1).toString() + ":30"
                            thour = 9 + i / 2 - 1
                            tminute = 30
                            textview.textAlignment = View.TEXT_ALIGNMENT_CENTER
                        } else if (i % 2 == 1) {
                            textview.text = (9 + i / 2).toString() + ":00"
                            textview.textAlignment = View.TEXT_ALIGNMENT_CENTER
                            thour = 9 + i / 2
                            tminute = 0
                        }
                    }
                    if(day == CalendarDay.today()){
                        if (thour < hour.toInt() || (thour == hour.toInt() && tminute < min.toInt())) {
                            register[i][j] = 2;
                            if (i != 0) textview.text = "x"
                        }else{
                            for(u in reserve_room){
                                when(u){
                                    "1호실"->{
                                        if(j==0){
                                            for(k in cube){
                                                if((thour>=k.dateList[0].time.hour_start && tminute>=k.dateList[0].time.minute_start)&&(thour<=k.dateList[k.dateList.size-1].time.hour_end && tminute <= k.dateList[k.dateList.size-1].time.minute_end)  ){
                                                    register[i][j] = 2
                                                    if (i != 0) textview.text = "x"
                                                }
                                            }
                                        }
                                    }
                                    "2호실"->{
                                        if(j==1){
                                            for(k in cube){
                                                if((thour>=k.dateList[0].time.hour_start && tminute>=k.dateList[0].time.minute_start)&&(thour<=k.dateList[k.dateList.size-1].time.hour_end && tminute <= k.dateList[k.dateList.size-1].time.minute_end) ){
                                                    register[i][j] = 2
                                                    if (i != 0) textview.text = "x"
                                                }
                                            }
                                        }
                                    }
                                    "3호실"->{
                                        if(j==2){
                                            for(k in cube){
                                                if((thour>=k.dateList[0].time.hour_start && tminute>=k.dateList[0].time.minute_start)&&(thour<=k.dateList[k.dateList.size-1].time.hour_end && tminute <= k.dateList[k.dateList.size-1].time.minute_end) ){
                                                    register[i][j] = 2
                                                    if (i != 0) textview.text = "x"
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                row.addView(textview)
            }

            tableLayout.addView(row)
            //Toast.makeText(this.context, rows.toString(), Toast.LENGTH_LONG).show()
        }
        linearLayout.removeAllViewsInLayout()
        linearLayout.addView(tableLayout)
    }


    @SuppressLint("ResourceAsColor")
    fun fill_table(view: View) {
        for (i in 0 until row) {
            for (j in 0 until column) {
                var str = "$i$j"
                var textview = view.findViewById<TextView>(str.toInt())
                if (i != 0) {
                    textview.setOnClickListener {
                        if (register[i][j] == 0) {
                            textview.setBackgroundColor(R.color.myGreen)
                            register[i][j] = 1
                            map_register.put(i,j)
                            var t  = textview.text.split(":")
                            var nextindex = i+1
                            var next_text:TextView?=null
                            if(nextindex>row){
                                next_text!!.text = "22:00"
                            }else{
                                 next_text = view.findViewById<TextView>("$nextindex$j".toInt())
                            }
                            var nextt = next_text!!.text.split(":")
                            time.add(MyTime(t[0].toInt(),t[1].toInt(),nextt[0].toInt(),nextt[1].toInt()))
                            Listener!!.onSetReserveTime(MyTime(t[0].toInt(),t[1].toInt(),nextt[0].toInt(),nextt[1].toInt()))
                            when(j){
                                0->{
                                    Listener!!.setRoomNumber("1호실")
                                }1->{
                                Listener!!.setRoomNumber("2호실")
                            }2->{
                                Listener!!.setRoomNumber("3호실")
                            }
                            }
                        } else if (register[i][j] == 1) {
                            textview.setBackgroundColor(Color.TRANSPARENT)
                            textview.setBackgroundResource(R.drawable.border)
                            register[i][j] = 0
                            map_register.remove(i)
                            var t  = textview.text.split(":")
                            for(i in time){
                                if(i.hour_start==t[0].toInt() && i.minute_end ==t[1].toInt()){
                                   time.remove(i)
                                    Listener!!.removeReserveTime(i)
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(activity != null && activity is OnReserve1 ){
            Listener = activity as OnReserve1
        }
    }

}




