package com.szxb.zibo.moudle.zibo

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import com.google.gson.Gson
import com.hao.lib.Util.FileUtils
import com.hao.lib.Util.MiLog
import com.hao.lib.Util.StatusBarUtil
import com.hao.lib.Util.ThreadUtils
import com.hao.lib.base.Rx.Rx
import com.szxb.java8583.module.manager.BusllPosManage
import com.szxb.jni.SerialCom
import com.szxb.zibo.BuildConfig
import com.szxb.zibo.R
import com.szxb.zibo.base.BusApp
import com.szxb.zibo.base.BaseActivity
import com.szxb.zibo.base.Task
import com.szxb.zibo.cmd.DoCmd
import com.szxb.zibo.cmd.devCmd
import com.szxb.zibo.config.haikou.ConfigContext
import com.szxb.zibo.config.zibo.InitConfigZB
import com.szxb.zibo.config.zibo.Result
import com.szxb.zibo.config.zibo.line.PraseLine
import com.szxb.zibo.moudle.function.card.CardInfoEntity
import com.szxb.zibo.moudle.function.card.PraseCard
import com.szxb.zibo.moudle.function.location.GPSEvent
import com.szxb.zibo.moudle.init.InitActiivty.init21Time
import com.szxb.zibo.moudle.maintool.HistoryAdapter
import com.szxb.zibo.moudle.maintool.MainToolAdapter
import com.szxb.zibo.moudle.maintool.ParamShowInfo
import com.szxb.zibo.record.XdRecord
import com.szxb.zibo.util.BusToast
import com.szxb.zibo.util.DateUtil
import com.szxb.zibo.util.ZipUtils
import com.szxb.zibo.util.sp.CommonSharedPreferences
import com.szxb.zibo.voice.SoundPoolUtil
import com.szxb.zibo.voice.VoiceConfig
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.main
import kotlinx.android.synthetic.main.param_layout.*
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Main2Activity : BaseActivity() {

    var sendStationInfo = 1;
    var operate = 0//未操作的时间间隔
    var viewTag = 0//1 菜单  2 历史记录  3 参数  0 主页无任何附加页面
    var sdCardpath = "/storage/sdcard1"
    var am: AudioManager? = null
    var heartTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("流程", "进入main")
        setContentView(R.layout.activity_main)
        try {
            main.setBackgroundResource(R.mipmap.background)
            initView()
            updateView()
            now_time.setOnClickListener {
                SerialCom.DevRest()//重启k21
            }
            am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        } catch (e: java.lang.Exception) {
            Log.i("错误", "main 初始化错误")
        }

        sign.setOnClickListener {
            BusApp.getPosManager().driverNo = "12345667"
        }

        now_time.setOnClickListener {
            Thread {
                val str = "{\"customer_code\":\"881641\",\"sign\":\"6E7DA68D54DD8C30D5C51D34EED7C37D\",\"req_type\":\"1\",\"timestamp\":\"1576805238388\",\"result_msg\":\"请求成功！\",\"merchant_no\":\"001\",\"seq_no\":\"20191220000122\",\"server_time\":\"1576805238\",\"charset\":\"UTF-8\",\"trans_data\":{\"request_code\":\"apk\",\"server_time\":\"1576805238\",\"task_no\":\"8f270015a6bf4508a425293830faa42d\",\"request_content\":{\"protocol\":\"fastdfs\",\"packet_size\":\"24040460\",\"httpUrl\":\"http://139.9.113.219:10091/group1/M00/00/06/rBQABV38Pa-AMyxZAXEuFBgV8gk903.apk\",\"group_name\":\"group1\",\"url\":\"Fastdfs://139.9.113.219:22000/group1/M00/00/05/rBQABV34lKmAMxnUAW7UDFjEyqA497.apk/1.0.1_191217_zibo.apk\",\"md5\":\"737894f461908c0b185e8eb4ffe892be\"}},\"channel_code\":\"0000001\",\"version\":\"1.0.2\",\"result_code\":\"0\",\"terminal_type\":\"Q6-B\",\"terminal_no\":\"Q6B0B1T219140389\",\"sign_type\":\"MD5\"}\n";
                var r = Gson().fromJson<Result>(str, Result::class.java);
                InitConfigZB.downLoadFile(r, r.trans_data.task_no);
            }.start()
        }

//        PraseLine.praseUsrByte(FileUtils.readAssetsFileTobyte("20200726121107.usr", BusApp.getInstance()), "20200726121107.usr");
    }

    private fun updateView() {
        ThreadUtils.getInstance().createSch("mian").scheduleWithFixedDelay(Runnable {
            runOnUiThread {
                try {
                    operate++
                    refreshView()
                    if (operate >= 15) {//6s钟未操作就重置界面
                        refreshMoudle()
                    }
                } catch (e: Exception) {
                    Log.i("界面更新", "失败：" + e.message);
                }
            }
        }, 0, 1, TimeUnit.SECONDS)
    }

    //更新UI
    fun refreshView() {
        try {
            now_time.text = DateUtil.getCurrentDate();
            version_tx.text = BuildConfig.BIN_NAME + "\n[" + BusApp.getInstance().getPakageVersion() + "]"
            version_tx.append("\n" + BusApp.getPosManager().m1psam + "\n" + BusApp.getPosManager().cpupsam + "\n" + BusApp.getPosManager().jtBpsam)
            white.text = "白名单：" + CommonSharedPreferences.get("white", "未获取到当前黑名单版本") as String
            black.text = "黑名单：" + CommonSharedPreferences.get("black", "未获取到当前白名单版本") as String
            busno.text = "车辆号：" + BusApp.getPosManager().busNo
            pos_num.text = "设备号：" + BusApp.getPosManager().getPosSN()
            if (BusApp.downProgress == 0) {
                down_progress.visibility = View.GONE
            } else if (BusApp.downProgress == -1) {
                down_progress.visibility = View.VISIBLE
                down_progress.text = "下载失败"
            } else {
                down_progress.visibility = View.VISIBLE
                down_progress.text = "" + BusApp.downProgress + "%"
            }

            if (!BusApp.getPosManager().driverNo.equals("00000000")) {//不刷司机卡也能上班
                is_sign.visibility = View.VISIBLE
                no_sign.visibility = View.GONE
                line.text = BusApp.getPosManager().getLineName() + "   " + BusApp.getPosManager().lineNo
                if (!BusApp.getPosManager().lineType.equals("O")) {
                    station.text = BusApp.getPosManager().stationName + "(" + BusApp.getPosManager().stationID + ")"
                    station.visibility = View.VISIBLE
                } else {
                    station.visibility = View.GONE
                }
                if (BusApp.getPosManager().basePrice == 0) {
                    price.text = "暂无票价";
                    if (!BusApp.getPosManager().lineType.equals("O")) {
                        BusApp.getPosManager().basePrice = PraseLine.getMorePayPrice(null, true, true)
                    }
                } else {
                    price.text = FileUtils.fen2Yuan(BusApp.getPosManager().basePrice)
                }
            } else {
                is_sign.visibility = View.GONE;
                no_sign.visibility = View.VISIBLE;
            }
            if (BusApp.getInstance().netWorkState) {
                net.visibility = View.GONE
            } else {
                net.visibility = View.VISIBLE
            }

            if (BusApp.getPosManager().lineType.equals("P")) {
                diraction.visibility = View.VISIBLE
                diraction.text = if (BusApp.getPosManager().direction.equals("0001")) "上行" else "下行"
            } else {
                diraction.visibility = View.GONE
            }


        } catch (e: Exception) {
            Log.i("界面设置报错了", "报错了" + e.message)
        }
    }

    fun refreshMoudle() {
        viewTag = 0
        if (tools_list.visibility == View.VISIBLE) {
            setViewAnimal(tools_list, false)
        }

        if (history_list.visibility == View.VISIBLE) {
            setViewAnimal(history_list, false)
        }

        if (param_layout.visibility == View.VISIBLE) {
            setViewAnimal(param_layout, false)
        }
    }

    private fun initView() {
        sign.setOnClickListener { BusApp.getPosManager().driverNo = "12345678" }

        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.black))
        main.setBackgroundResource(R.mipmap.background)
        var tools: MutableList<String> = arrayListOf()
        tools.add("机器参数")
        tools.add("刷卡记录")
        tools.add("TX二维码记录")
        tools.add("银联记录")
        tools.add("支付宝记录")
        tools.add("更新初始化的参数")
        tools.add("导出日志")
        tools.add("导入黑名单")
        tools.add("导入线路")
        tools.add("选择线路")
        tools.add("当前操作：" + (if (BusApp.getPosManager().operate == 0) "自动" else "手动"))
        tools.add("设置车号")
        tools.add("采集GPS")
        tools.add("当前机器：" + (if (BusApp.getPosManager().posUpDate == 1) "前车机" else "后车机"))
        tools_list.layoutManager = LinearLayoutManager(this)
        tools_list.adapter = MainToolAdapter(this, tools)

        history_list.layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        )
        history_list.layoutManager = LinearLayoutManager(this)
        history_list.adapter = HistoryAdapter(this)
    }

    internal var nowStation = 0
    fun keyUp() {
        if (viewTag == 0) {
            if (BusApp.getPosManager().lineType.equals("O")) {
                am!!.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            } else {
                if (BusApp.getPosManager().operate == 0) {
                    am!!.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                } else {
                    InitConfigZB.addstation()
                }
            }
        }

        if (tools_list.visibility == View.VISIBLE) {
            (tools_list.adapter as MainToolAdapter).setSelectRefuse()
            tools_list.scrollToPosition((tools_list.adapter as MainToolAdapter).check)
        }

        if (history_list.visibility == View.VISIBLE) {
            (history_list.adapter as HistoryAdapter).beforPage()
        }
    }

    fun keyDown() {
        if (viewTag == 0) {
            if (BusApp.getPosManager().lineType.equals("O")) {
                am!!.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI)
            } else {
                if (BusApp.getPosManager().operate == 0) {
                    am!!.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI)
                } else {
                    InitConfigZB.refuseStation()
                }
            }
        }


        if (tools_list.visibility == View.VISIBLE) {
            (tools_list.adapter as MainToolAdapter).setSelectAdd()
            tools_list.scrollToPosition((tools_list.adapter as MainToolAdapter).check)
        }

        if (history_list.visibility == View.VISIBLE) {
            (history_list.adapter as HistoryAdapter).nextPage()
        }
    }

    override fun rxDo(tag: Any, vararg o: Any?) {
        super.rxDo(tag, o)
        if (tag is String) {
            if (o.size > 0) {
                when (o[0]) {
//                "setRight" -> PraseCard.checkMac(DoCmd.checkMac(o as ByteArray))
                    ConfigContext.KEY_BUTTON_BOTTOM_LEFT -> {
                        operate = 0
                        keyDown()
                    }
                    ConfigContext.KEY_BUTTON_BOTTOM_RIGHT
                    -> {
                        operate = 0
                        keyCancal()
                    }
                    ConfigContext.KEY_BUTTON_TOP_LEFT
                    -> {
                        operate = 0
                        keyUp()
                    }
                    ConfigContext.KEY_BUTTON_TOP_RIGHT
                    -> {
                        operate = 0
                        keyComfir()
                    }
                }
            }

            when (tag) {
                "sdCardIn" -> {//sd卡插入
                    sdCardpath = o[0] as String
                }

                "mianParam" -> {
                    try {
                        refreshParam(o[0] as ParamShowInfo)
                    } catch (e: java.lang.Exception) {
                        Log.i("参数接收出错", o.toString())
                    }
                }
                "checkMac" -> {
                    try {
//                        arrayOf<Any>(cardInfoEntity, xdRecord)
                        MiLog.i("刷卡", "校验  发送校验命令")
                        var cardInfoEntity = o[0] as CardInfoEntity
                        var xdRecord = o[1] as XdRecord
                        var transaction_num: String = "00"
                        var transaction_type: String = "00"
                        if (cardInfoEntity.selete_aid.equals("03")) {
                            transaction_num = cardInfoEntity.file18LocalInfoEntity.transaction_number_18
                            transaction_type = cardInfoEntity.file18LocalInfoEntity.transaction_type
                        } else if (cardInfoEntity.selete_aid.equals("02")) {
                            transaction_num = cardInfoEntity.file18NewCPUInfoEntity.transaction_number_18
                            transaction_type = cardInfoEntity.file18NewCPUInfoEntity.transaction_type
                        }
                        PraseCard.checkMacBack(DoCmd.checkMac(FileUtils.hex2byte(transaction_num + transaction_type)), cardInfoEntity, xdRecord)
                    } catch (e: java.lang.Exception) {
                        MiLog.i("刷卡", "mainactivity校验出错了：" + e.message)
                    }
                }

                "lockNewCpu" -> {
                    DoCmd.lockNewCpu()
                }
                "myselfAddress" -> {
                    if ((o[0] as String?)!!.contains("失败")) {
                        BusToast.showToast(o[0] as CharSequence?, false)
                    } else {
                        BusToast.showToast(o[0] as CharSequence?, true)
                        SoundPoolUtil.play(VoiceConfig.shuamachenggong)
                    }
                }
                "keyboardAddress" -> {
                    DoCmd.setAddress(o[0] as String?)
                }

                "sendStationInfo" -> {
                    var time: Long = 0
                    if (BusApp.getPosManager().getPosUpDate() == 1) {
                        time = 50
                    } else {
                        time = 300
                    }
                    Handler().postDelayed(Runnable { DoCmd.sendStationInfo() }, time)
                }
            }
        }

    }


    private fun keyComfir() {
        Log.i("当前状态", "" + viewTag)
        if (viewTag == 0) {
            viewTag = 1
            setViewAnimal(tools_list, true)
        } else if (viewTag == 1) {
            setViewAnimal(tools_list, false)
            var ad = tools_list.adapter
            Log.i("当前状态  按键选取", "" + (ad as MainToolAdapter).check)
            when ((ad as MainToolAdapter).check) {
                0 -> {//机器参数
                    initParam()
                    viewTag = 3
                    setViewAnimal(param_layout, true)
                }
                1 -> {
                    //刷卡记录
                    setViewAnimal(history_list, true)
                    viewTag = 2
                    (history_list.adapter as HistoryAdapter).setType(0)
                }
                2 -> {
                    //TX二维码记录
                    setViewAnimal(history_list, true)
                    viewTag = 2
                    (history_list.adapter as HistoryAdapter).setType(1)
                }
                3 -> {
                    //银联记录
                    setViewAnimal(history_list, true)
                    viewTag = 2
                    (history_list.adapter as HistoryAdapter).setType(2)
                }
                4 -> {
                    //AL二维码记录
                    setViewAnimal(history_list, true)
                    viewTag = 2
                    (history_list.adapter as HistoryAdapter).setType(3)
                }

                5 -> {
                    Thread {
                        //更新初始化的参数
                        BusApp.getPosManager().csnVer = "00000000000000"
                        BusApp.getPosManager().usrver = "00000000000000"
                        BusApp.getPosManager().farver = "00000000000000"
//            posInfoDate.setFar_ver("00000000000000"
                        BusApp.getPosManager().pub_ver = "00000000000000"
                        BusApp.getPosManager().linver = "00000000000000"
                        BusApp.getPosManager().ums_key_ver = "00000000000000"
                        InitConfigZB.sendInfoToServer(InitConfigZB.INFO_UP)
                        refreshMoudle()
                    }.start()
                }
                6 -> {//导出历史数据
                    Thread {
                        try {
                            BusToast.showToast("正在导出日志", true)
                            FileUtils.copyDir(
                                    Environment.getExternalStorageDirectory().toString() + "/log/",
                                    "/storage/sdcard1/Log"
                            )
                            //导出数据库文件
                            val posDirectory = "data/data/" + BusApp.getInstance().packageName + "/databases"
                            val sdDirectory = "/databases/" + BusApp.getPosManager().busNo

                            FileUtils.copyDir(
                                    "data/data/" + BusApp.getInstance().packageName,
                                    "/storage/sdcard1/date"
                            )
                            BusToast.showToast("导出成功", true)
//                          FileUtils.delFolder(Environment.getExternalStorageDirectory().toString() + "/NewRecord")
                        } catch (e: java.lang.Exception) {
                            MiLog.i("按键功能", "导出文件出错" + e.message)
                            BusToast.showToast("导出失败,请检查SD卡状态", true)
                        }
                    }.start()
                    refreshMoudle()
                }
                7 -> {//导入黑名单
                    Thread {
                        try {
                            BusToast.showToast("正在获取黑名单数据", true)
                            val black = FileUtils.searchFile(sdCardpath, ".csn")//黑名单
                            if (black != null && !black.equals("")) {
                                PraseLine.praseCsn(File(black))
                                BusToast.showToast("黑名单导入成功", true)
                            } else {
                                BusToast.showToast("黑名单导入异常，请检查SD卡状态和黑名单文件", true)
                            }
                        } catch (e: Exception) {
                            BusToast.showToast("导入失败，请检查黑名单文件是否异常", false)
                        }
                    }.start()
                    refreshMoudle()
                }

                8 -> {
                    refreshMoudle()
                    Thread {
                        try {
                            BusToast.showToast("正在获取线路数据", true)
                            val line = FileUtils.searchFile(sdCardpath, ".lin")
                            if (line != null && !line.equals("")) {
                                //TODO 本地设置线路
                                PraseLine.praseAllLine(File(line))
                                BusToast.showToast("线路导入成功", true)
                            } else {
                                BusToast.showToast("白名单导入异常，请检查SD卡状态和白名单文件", true)
                            }
                        } catch (e: Exception) {
                            BusToast.showToast("导入失败,请检查线路文件是否异常", false)
                        }
                    }.start()
                }

                9 -> {
                    refreshMoudle()
                    startActivity(Intent(this, SelectLineActivity::class.java))
                }

                10 -> {
                    BusApp.getPosManager().operate = (BusApp.getPosManager().operate + 1) % 2
                    initView()
                    viewTag = 0
                }

                11 -> {
                    refreshMoudle()
                    startActivity(Intent(this, SetBusNuActivity::class.java))
                }
                12 -> {
                    refreshMoudle()
                    startActivity(Intent(this, GPSColletActivity::class.java))
                }
                13 -> {
                    refreshMoudle()
                    if (BusApp.getPosManager().posUpDate == 1) {
                        BusApp.getPosManager().posUpDate = 2
                    } else {
                        BusApp.getPosManager().posUpDate = 1
                    }
                    initView()
                }
            }
        }
    }

    private fun keyCancal() {
        refreshMoudle()
    }

    fun initParam() {
        Thread {
            try {
                var paramShowInfo = ParamShowInfo()
                paramShowInfo.driver_no = BusApp.getPosManager().driverNo
                paramShowInfo.busno = BusApp.getPosManager().busNo
                paramShowInfo.linename = BusApp.getPosManager().lineName
                paramShowInfo.lineid = BusApp.getPosManager().lineNo
                paramShowInfo.binver = BusApp.getPosManager().binVersion
                paramShowInfo.appver = BusApp.getInstance().pakageVersion
                paramShowInfo.unionmac = BusllPosManage.getPosManager().mchId
                paramShowInfo.unionpos = BusllPosManage.getPosManager().posSn
                paramShowInfo.linever = BusApp.getPosManager().linver
                paramShowInfo.csnVer = BusApp.getPosManager().csnVer //黑名单
                paramShowInfo.farver = BusApp.getPosManager().farver  //票价
                paramShowInfo.loction = if (GPSEvent.bdLocation == null) "0" else GPSEvent.bdLocation.longitude.toString() + "," + GPSEvent.bdLocation.latitude
                paramShowInfo.pubver = BusApp.getPosManager().pub_ver //密钥
                paramShowInfo.mykeyborad = BusApp.getPosManager().myselfkeybroadAddress
                paramShowInfo.keyborad = BusApp.getPosManager().keybroadAddress
                Rx.getInstance().sendMessage("mianParam", paramShowInfo)
            } catch (e: java.lang.Exception) {
                Log.i("界面数据刷新失败", "失败")
            }
        }.start()
    }

    fun refreshParam(paramShowInfo: ParamShowInfo) {
        try {
            driver_no.text = paramShowInfo.driver_no;
            bus_no.text = paramShowInfo.busno
            line_name.text = paramShowInfo.linename
            far_ver.text = paramShowInfo.farver
            line_id.text = paramShowInfo.lineid
            bin_ver.text = paramShowInfo.binver
            app_ver.text = paramShowInfo.appver
            union_pos.text = paramShowInfo.unionpos
            union_mch.text = paramShowInfo.unionmac
            location_now.text = paramShowInfo.loction
            lin_ver.text = paramShowInfo.linever
            key_ver.text = paramShowInfo.pubver
            myself_keyboardAddress.text = paramShowInfo.mykeyborad
            keyboard_Address.text = paramShowInfo.keyborad
        } catch (e: Exception) {
            Log.i("界面设置报错了", "报错了");
        }
    }

    override fun onResume() {
        super.onResume()
        ThreadUtils.getInstance().createSingle("time").submit(Runnable { init21Time() })
        Task.runTask()
    }
}

