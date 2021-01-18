package com.szxb.zibo.moudle.zibo

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.szxb.java8583.module.manager.BusllPosManage
import com.szxb.jni.SerialCom
import com.szxb.lib.Util.FileUtils
import com.szxb.lib.Util.MiLog
import com.szxb.lib.Util.StatusBarUtil
import com.szxb.lib.Util.ThreadUtils
import com.szxb.lib.base.Rx.Rx
import com.szxb.zibo.BuildConfig
import com.szxb.zibo.R
import com.szxb.zibo.base.BaseActivity
import com.szxb.zibo.base.BusApp
import com.szxb.zibo.base.Task
import com.szxb.zibo.cmd.DoCmd
import com.szxb.zibo.config.haikou.ConfigContext
import com.szxb.zibo.config.zibo.DBManagerZB
import com.szxb.zibo.config.zibo.InitConfigZB
import com.szxb.zibo.config.zibo.line.PraseLine
import com.szxb.zibo.moudle.function.card.CardInfoEntity
import com.szxb.zibo.moudle.function.card.ICCard.PraseICCard
import com.szxb.zibo.moudle.function.card.PraseCard
import com.szxb.zibo.moudle.function.location.GPSEvent
import com.szxb.zibo.moudle.init.InitActiivty.init21Time
import com.szxb.zibo.moudle.maintool.HistoryAdapter
import com.szxb.zibo.moudle.maintool.MainToolAdapter
import com.szxb.zibo.moudle.maintool.ParamShowInfo
import com.szxb.zibo.record.XdRecord
import com.szxb.zibo.util.BusToast
import com.szxb.zibo.util.DateUtil
import com.szxb.zibo.util.sp.CommonSharedPreferences
import com.szxb.zibo.voice.SoundPoolUtil
import com.szxb.zibo.voice.VoiceConfig
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.main
import kotlinx.android.synthetic.main.param_layout.*
import java.io.File
import java.util.concurrent.TimeUnit

class Main2Activity : BaseActivity() {

    var operate = 0//未操作的时间间隔
    var viewTag = 0//1 菜单  2 历史记录  3 参数  0 主页无任何附加页面
    var sdCardpath = "/storage/sdcard1"
    var am: AudioManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("流程", "进入main")
        Log.i("升级", "当前版本" + BusApp.getInstance().pakageVersion)

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
            var date = FileUtils.hexStringToBytes("01000400ed904268b38220002000200020002000200020002000200020002000200032003000310031003900350038003000350030003200715c1c4e0177e07a184e025ecc53715c578853909e528b4e0459d56e0b67516751677f89ef8d32003400f7532000200020002000200020002000200020002000200020002000330037003000310032003200310039003500380030003500300032003400380034003400e07a184e025e6c51895b405c200020002000200020002000200020002000320030003000350031003100300036007f951f67200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000574c66007e00320000ff851d5151513e710dd564f30f654d7a1450470511eac30c608564c794e754535ed71f7139b2cac53235a885682f7ddf916bda8b9d9bd398234628c1e5288a2afc01cb8d1cfc4890988fd54b09b96af651aed65251515a3e9540b4d21ba9fbc567d2dc6a825a5e2b639a38eacf8cf45ececeb4d1287e8a3f531104121bff91502795a8dd991948d46b063caae63361bbb4c29002b604edeed9ea7123b9c41e0b80d38b2e72e9db30f9c0b85b5377bfe2bfc6d20ddcb440eafed5934ba16eb7414363571058d923b24423e3e55ba5ed62b9e7ad9b0b9a1b41e5aaccdf080bbee880140bed0c1d84366aeab063eaebf6eaf6fff01c6cfb8b3144fb5ccb30e92ede7f1c02b8be654170a6d45f2778a8c2326c2db5b2ae510720d1ae51b700270ecc6672c13fd7533a56d867f0542bfae2b77a8022c2ceb7263139d370a3160d92202352bb83a444e17ea5eab93d7a1d0641f6ec5d87f13c4441c57ce8a297f0c7843ec59892f59582234f64fa2ed5a69b655b6d9a6b8ccfde615ad603fc0d89c217e473eb85d87db7fcbd1410b8df803224492e9c2f21b720ff39254411d58386cbc73654c4c26239935ab802ba8ebf4f21288583ebd1f1dde0349bd5cba8504be0e7006578d30461bafc2aea7ea9fd8e721db692427e6700f57fec5bbfd0a436f841ac5a49834cd91242e57188717eca58488186aa6b09f0fd32e72db5169710af748550a2c14799d95a08b5984ef80e463f03e0d988b5989db72673f8a42d2fbad82dedf09276b0c3f635ac9d72402d41394c9daa23d141efc2d4eb098a2bae51c377fb8527c7ae51025fbd9c251b8003bcbfee276e677d7d8add0314f667323b6ff2becf555db39c373981ba43a1234a2fb76ff5d06dc39b574ed6625978334320058274476ba3f917774833822ec0233e885f77128811e3a220ffa167678f1d7e73a7599e1d042278568cebc3e08a9b32bde5b83266cd68de563afeb86afa98003c4f6942b2647c0ed4ea16194f76d994c6f21183dedadff0771f6a56b760eac7c1eebbd0c95bdb8de366dfe0a88b39fb2e4f1e99cd979cdf405237c086bc249d3cedecda744de7b1672433d5042a6db5321cfe4c83884c7eaacf72728271441ddf3016e932c639549ab4b641a56efa29cb231876ee087f5dc3a7491169f2fb526409e25692657929ec4efc0ae01cdec6ac24b253ffaa48f2f2a6bb5a3e0093151403b91d2cb5eebe6cb1fe5c9ed90de5beee81de0f45b671215d0329863a08351b6dc599cea8efb2e71a02409d0f7bd07af35b75021a9e344ccd5bef357c414b253fa73dd61b369c704933df5a3e8163a70fc1276d8847c05fad9ec9cc633b3e494274722b46a6b4aa28624ac8a9cf9f8d644791ff5f78f8b73033241e0078d98544c173004cdfc32e53f82c2884f86d8574f957b0bb68")
            PraseICCard.praseIC(date)
//            DoCmd.startSearchICcard();
//            Thread {
//                val str = "{\"customer_code\":\"881641\",\"sign\":\"6E7DA68D54DD8C30D5C51D34EED7C37D\",\"req_type\":\"1\",\"timestamp\":\"1576805238388\",\"result_msg\":\"请求成功！\",\"merchant_no\":\"001\",\"seq_no\":\"20191220000122\",\"server_time\":\"1576805238\",\"charset\":\"UTF-8\",\"trans_data\":{\"request_code\":\"apk\",\"server_time\":\"1576805238\",\"task_no\":\"8f270015a6bf4508a425293830faa42d\",\"request_content\":{\"protocol\":\"fastdfs\",\"packet_size\":\"24040460\",\"httpUrl\":\"http://139.9.113.219:10091/group1/M00/00/06/rBQABV38Pa-AMyxZAXEuFBgV8gk903.apk\",\"group_name\":\"group1\",\"url\":\"Fastdfs://139.9.113.219:22000/group1/M00/00/05/rBQABV34lKmAMxnUAW7UDFjEyqA497.apk/1.0.1_191217_zibo.apk\",\"md5\":\"737894f461908c0b185e8eb4ffe892be\"}},\"channel_code\":\"0000001\",\"version\":\"1.0.2\",\"result_code\":\"0\",\"terminal_type\":\"Q6-B\",\"terminal_no\":\"Q6B0B1T219140389\",\"sign_type\":\"MD5\"}\n";
//               var r = Gson().fromJson<Result>(str, Result::class.java);
//               InitConfigZB.downLoadFile(r, r.trans_data.task_no);
//                MiLog.i("流程", "安装patch");
//                BusApp.getInstance().loadPatch(Environment.getExternalStorageDirectory().absolutePath + "/zibo.apk")
//卡解析
//            MiLog.i("刷卡", FileUtils.bytesToHexString(bytes));
//            var xdRecord = XdRecord().praseDate("0200151F010401FDFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF34303032353133303330333433303339333933393338010000000000000000FFFFFFFF513642394131543231373438303732313700200137754131061623690000000000000061026503104930902000001577202012301241035D000000240000003C0000007423000037009583417AFB0100004530000366453065020103000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001F5A")
//
//
//            MiLog.i("刷卡", FileUtils.bytesToHexString(bytes));
//            var cardInfoEntity = CardInfoEntity();
//            cardInfoEntity.putDate(FileUtils.hexStringToBytes("000000202012282145280800705d14012012788080024d54009d0040869b19705d14010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001493100100000134903674550ffffffff020103104931001000001349202009162040123159440000015645004550000101007900000000000000094131061636752020122821451109000037002009542800000000000000780000000000000dc420201228214511453013664530ffffffff2cf5b6dfa1f227027d010100000000000000012601453013664530ffffffff453000fb03303034303939393800004131061636752020122821451100000030ab00000000000000000000453000000000000000000000000000000000000000000000000000000000000c0000284000210050000000000000000000000000000000000000000000000dc4"));
//            var cardInfoEntity1 = CardInfoEntity();
//            cardInfoEntity1.putDate(FileUtils.hexStringToBytes("000000202012282145310800822c87a120127833b0024d54009d0042869b19822c87a10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001493150100000014003724710ffffffff020103104931501000000140202008242040123150000000015645004710000101005f000000000000060941310616237020201228214342090000370020067190000000000000005e0000000600000dc420201228214342453013664530ffffffff2cf5b6dfa1f227027d010100000000000000033602453013664530ffffffff453000fb01303034303939393900004131061636742020122817115900000000ab453013664530ffffffff453000fb0330303430393939390000413106163674202012281712220000000c0000284000210064000000000000000000000000000000000000000000000dc4"));
//            Thread {
//                FileUtils.copyFile("/storage/sdcard1/zibo_1.5.1.apk", "/storage/sdcard0/oldVersion.apk")
//            }
//            BusApp.getInstance().cleanParch()
//            }.start()
        }
//        PraseLine.praseUsrByte(FileUtils.readAssetsFileTobyte("20200726121107.usr", BusApp.getInstance()), "20200726121107.usr");

//        BusllPosManage.getPosManager().setMachId("438370341112007")
//        BusllPosManage.getPosManager().key = "2C4F497C20FB2FB6758651E5750DAD9B"
//        BusllPosManage.getPosManager().posSn = "63461748"
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
            if (System.currentTimeMillis() - DoCmd.runtime > 3000) {
                DoCmd.startSearchICcard();
            }
            var unupRecord = DBManagerZB.checkUnUp()
            if (unupRecord > 100) {
                BusToast.showToast("当前未上传记录" + unupRecord + "条", false);
            }
            now_time.text = DateUtil.getCurrentDate();
            var versionInfo = BuildConfig.BIN_NAME + "\n[" + BusApp.getInstance().getPakageVersion() + "]"
            if (!TextUtils.isEmpty(BusApp.getPosManager().upDateInfo)) {
                versionInfo += ("\n[" + BusApp.getPosManager().upDateInfo + "]")
            }
            version_tx.text = versionInfo
            version_tx.append("\n" + BusApp.getPosManager().m1psam + "\n" + BusApp.getPosManager().cpupsam + "\n" + BusApp.getPosManager().jtBpsam)
            white.text = "白名单：" + CommonSharedPreferences.get("usrver", "未获取到当前白名单版本") as String
            black.text = "黑名单：" + CommonSharedPreferences.get("csnVer", "未获取到当前黑名单版本") as String
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
        tools.add("身份证记录")
        tools.add("更新初始化的参数")
        tools.add("导出日志")
        tools.add("导入黑名单")
        tools.add("导入线路")
        tools.add("选择线路")
        tools.add("当前操作：" + (if (BusApp.getPosManager().operate == 0) "自动" else "手动"))
        tools.add("设置车号")
        tools.add("采集GPS")
        tools.add("当前机器：" + (if (BusApp.getPosManager().posUpDate == 1) "前车机" else "后车机"))
        tools.add("清理日志")
        tools.add("打开身份证识别")
        tools.add("升级版本")

        tools_list.layoutManager = LinearLayoutManager(this)
//        tools_list.layoutManager = LinearLayoutManager(this)
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
                "resetPSAM" -> {
                    DoCmd.resetPSAM()
                }
                "closeport" -> {
                    DoCmd.closePort1(byteArrayOf())
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

                "doCardProcess" -> {
                    DoCmd.doCardProcess()
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
                    //身份证记录
                    setViewAnimal(history_list, true)
                    viewTag = 2
                    (history_list.adapter as HistoryAdapter).setType(4)
                }

                6 -> {
                    Thread {
                        //更新初始化的参数
                        MiLog.i("流程", "按键初始化参数 初始化  线路版本  票价版本")
                        BusApp.getPosManager().clearRunParam()
                        InitConfigZB.sendInfoToServer(InitConfigZB.INFO_UP)
                        refreshMoudle()
                    }.start()
                }
                7 -> {//导出历史数据
                    Thread {
                        try {
                            BusToast.showToast("正在导出日志", true)
                            FileUtils.copyDir(
                                    Environment.getExternalStorageDirectory().toString() + "/log/",
                                    "/storage/sdcard1/Log"
                            )
                            //导出数据库文件
                            val posDirectory = "data/data/" + BusApp.getInstance().application.packageName + "/databases"
                            val sdDirectory = "/databases/" + BusApp.getPosManager().busNo

                            FileUtils.copyDir(
                                    "data/data/" + BusApp.getInstance().application.packageName,
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
                8 -> {//导入黑名单
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

                9 -> {
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

                10 -> {
                    refreshMoudle()
                    startActivity(Intent(this, SelectLineActivity::class.java))
                }

                11 -> {
                    BusApp.getPosManager().operate = (BusApp.getPosManager().operate + 1) % 2
                    initView()
                    viewTag = 0
                }

                12 -> {
                    refreshMoudle()
                    startActivity(Intent(this, SetBusNuActivity::class.java))
                }
                13 -> {
                    refreshMoudle()
                    startActivity(Intent(this, GPSColletActivity::class.java))
                }
                14 -> {
                    refreshMoudle()
                    if (BusApp.getPosManager().posUpDate == 1) {
                        BusApp.getPosManager().posUpDate = 2
                    } else {
                        BusApp.getPosManager().posUpDate = 1
                    }
                    initView()
                }
                15 -> {//清理日志
                    refreshMoudle()
                    Thread {
                        MiLog.clear(-1);
                    }.start()
                }
                16 -> {//打开串口
                    refreshMoudle()
                    DoCmd.openPort1(byteArrayOf())
                }

                17 -> {
                    refreshMoudle()
                    Thread {
                        try {
                            var file = File("/storage/sdcard1");
                            var filePath = FileUtils.searchFile("/storage/sdcard1", "zibo");
                            file = File(filePath);
                            if (file.exists() && file.length() < 10 * 1024 * 1024) {
                                BusToast.showToast("正在更新", true)
                                MiLog.i("流程", "正在加载补丁");
                                BusApp.getInstance().loadPatch(file.path)
                            } else {
                                BusToast.showToast("请检查增量包", false)
                            }
                        } catch (e: Exception) {
                            BusToast.showToast("更新失败," + e.message, false)
                        }
                    }.start()
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
            install_version.text = BuildConfig.InstallApk
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

