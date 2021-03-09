package com.me.dynamicanimation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.dynamicanimation.animation.SpringAnimation

class MainActivity : AppCompatActivity() {
    var image: ImageView? = null
    var imageX=0f
    var imageY=0f
    var velocityTracker:VelocityTracker?=null
    var stiffnessBar:SeekBar?=null
    var dampingRatioBar:SeekBar?=null
    var imageXAnimation:SpringAnimation?=null
    var imageYAnimation:SpringAnimation?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        image = findViewById(R.id.image)
        stiffnessBar = findViewById(R.id.stiffness)
        dampingRatioBar = findViewById(R.id.dampingRatio)
        velocityTracker = VelocityTracker.obtain()
        stiffnessBar?.max=1000
        dampingRatioBar?.max = 1000
        findViewById<RelativeLayout>(R.id.content).let {
            it.setOnTouchListener(object:View.OnTouchListener{
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when(event?.action){
                        MotionEvent.ACTION_DOWN->{
                            imageX = event?.x
                            imageY = event?.y
                            velocityTracker?.addMovement(event)
                            return true
                        }
                        MotionEvent.ACTION_MOVE->{
                            image?.translationX=event.x-imageX
                            image?.translationY=event.y-imageY
//                            image?.x=event.x
//                            image?.y=event.y
                            velocityTracker?.addMovement(event)
                            return true
                        }
                        MotionEvent.ACTION_UP->{
                            if (image?.translationX!=0f){
                                imageXAnimation =
                                    SpringAnimation(image, SpringAnimation.TRANSLATION_X,0f)
                                imageXAnimation?.spring!!.stiffness=getStiffnessBar()
                                imageXAnimation?.spring!!.dampingRatio = getDampingRatioBar()
                                imageXAnimation?.setStartVelocity(velocityTracker?.xVelocity!!)
                                imageXAnimation?.start()
                            }
                            if (image?.translationY!=0f){
                                imageYAnimation =
                                    SpringAnimation(image,SpringAnimation.TRANSLATION_Y,0f)
                                imageYAnimation?.spring!!.stiffness =getStiffnessBar()
                                imageYAnimation?.spring!!.dampingRatio = getDampingRatioBar()
                                imageYAnimation?.setStartVelocity(velocityTracker?.yVelocity!!)
                                imageYAnimation?.start()
                            }
                        }
                        else->{
                            velocityTracker?.clear()
                            return true
                        }
                    }
                    return false
                }
            })
        }
    }
    fun getStiffnessBar():Float{
        return Math.max(stiffnessBar?.progress!!.toFloat(),1f)
    }
    fun getDampingRatioBar():Float{
        return dampingRatioBar?.progress!!/100f
    }
}