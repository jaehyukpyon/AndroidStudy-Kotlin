package com.example.ch11_jetpack

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ch11_jetpack.databinding.FragmentOneBinding
import com.example.ch11_jetpack.databinding.ItemRecyclerviewBinding

//recyclerview의 구성 요소 작성

// viewholder 역할 자체가 항목을 구성하는 뷰들을 가지는 역할
// 뷰바인딩 기법 이용 시, 뷰바인딩의 클래스 객체 자체가 뷰들을 갖고 있기 때문에 우리가 항목의 뷰들을 홀더에 일일이 나열할 필요가 없다
// 이 홀더 내에서는 val binding << 이렇게 선언해 준 변수로 바인딩 객체만 유지하고 있으면 OK
class MyViewHolder(val binding: ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)

// 각각의 항목을 구성하기 위한 Adapter
class MyAdapter(val datas: MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // Adapter를 생성하고 있는데, Adapter가 각각의 항목을 구성해주는 역할자
    // 그래서 fragment쪽에서 이용하겠습니다만, 항목을 구성하기 위한 데이터는 생성자를 통해 받는다
    // 또한 RecyclerView를 위한 Adapter이니, RecyclerView에서 제공되고 있는 Adapter를 상속받아서 클래스 선언

    override fun getItemCount(): Int {
        // 자동 호출되는 함수
        // recyclerview의 adapter가 항목을 구성해 주는데 항목 개수가 몇 개인지를 판단하기 위해서 자동 호출 된다. 적절한 항목 개수를 return 시켜주면 OK
        // 생성자 매개변수로 전달되는 매개 변수를 갖고 항목을 구성할 생각
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // recyclerview의 adapter는 holder를 이용하도록 되어 있음
        // 이 holder가 항목을 구성하기 위한 view를 가짐
        // 어느 holder를 이용할 것인지 이 onCreateViewHolder 함수 안에서 결정 해줘야 함
        // 즉 이 함수 내에서 이용하고자 하는 holder를 결정해서 return 시켜주게 되면, 그 holder가 적용 된다
        return MyViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // 각각의 항목을 어떻게 꾸밀 것인지
        // 꾸민다 라고 하면 각각의 항목을 위한 view가 있어야 하는데 그것은 onCreateViewHolder 함수 안에서 결정 된다
        // 그 다음에 그 항목에 data가 찍혀야 하고
        // 그 data는 MyAdapter의 생성자 매개변수로 전달된 data를 이용할 것

        // 이 함수도 자동 호출되는데, 항목 하나를 구성하기 위해서 >> 그럼 항목 하나를 구성하기 위한 view가 필요한데, 이 함수가 호출될 때 첫 번째 매개변수로 그 항목을 구성하기 위한 뷰를 갖고 있는 홀더 객체가 전달된다
        // 그러면 자동 전달 될텐데, 이 함수의 매개변수로 holder 객체가 어디로부터 전달 되어져서 이 함수의 매개 변수로 받느냐...? >> 내가 직접 연결시키지 않아도, 위에서 재정의한 함수 onCreateViewHolder 함수의 return 값이
        // onBindViewHolder 함수의 매개변수로 전달된다
        // 두 번째 매개변수 position은 몇 번째 항목을 구성하기 위해서 호출 한 것인지 index 값이 전달된다. 0은 첫 번재, 1은 두 번째 항목

        val binding = (holder as MyViewHolder).binding
        binding.itemData.text = datas[position] // recyclerview로 꾸미고자 하는 항목에는 문자열 하나만 나온다 >> item_recyclerview.xml에 문자열 하나만 출력되게끔...
    }
}

// 필수 구성 요소는 아닌 decoration 부분
class MyDecoration(val context: Context) : RecyclerView.ItemDecoration() {
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        // 항목이 찍힌 후에 마지막에 추가 꾸미기 작업

        // 전체 recyclerview의 사이즈
        val width = parent.width
        val height = parent.height

        // recyclerview의 항목 위에 이미지 하나 올리기 위해 이미지 먼저 받아낸 것
        val dr: Drawable? = ResourcesCompat.getDrawable(context.resources, R.drawable.kbo, null)
        // 이미지의 size 값 얻기
        val drWidth = dr?.intrinsicWidth
        val drHeight = dr?.intrinsicHeight

        // 이미지가 recyclerview에 출력되는 위치를 계산하기 위해 위에서 사이즈들을 얻은 것
        // 위치 값 계산 >> 화면 중간에 위치하도록
        val left = width / 2 - drWidth?.div(2) as Int
        val top = height / 2 - drHeight?.div(2) as Int

        // 이제 이미지를 그려주면 recyclerview에 나온다
        c.drawBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.kbo), left.toFloat(), top.toFloat(), null)
    }

    override fun getItemOffsets( // 각각의 항목을 꾸미기 위함
        outRect: Rect, // 항목이 출력되어야 하는 사각형
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        // 각각의 항목을 꾸미기 위해서 호출되는데, 몇 번째 항목을 꾸미기 위해서 call 된 것인지 획득
        val index = parent.getChildAdapterPosition(view) + 1 // 가장 첫 번째 항목은 0부터 시작한다
        if (index % 3 == 0) {
            outRect.set(10, 10, 10, 60) // 3으로 나누었을 때 나머지가 0이면 하단 방향으로 약간 더 떨어뜨려서 (세 개씩 묶여있는듯한 효과가 나타나게끔...)
        } else {
            outRect.set(10, 10, 10, 0)
        }

        view.setBackgroundColor(Color.parseColor("#28A0FF")) // 각 항목에 파란색
        ViewCompat.setElevation(view, 20.0f)
    }
}
// 여기까지가 recyclerview를 구성하기 위한 구성 요소 viewholder adapter decoration까지 만들었으면 이제 원 화면인 fragment쪽에서 recyclerview에 적용시키는 작업을 하면 된다... 아래 클래스에서...

class OneFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentOneBinding.inflate(inflater, container, false)

        val datas = mutableListOf<String>() // recyclerview를 구성하기 위한 가상 데이터

        for (i in 1..9) {
            datas.add("Item $i")
        }

        // adapter 준비
        val adapter = MyAdapter(datas)

        // layoutmanager
        val layoutManager = LinearLayoutManager(activity)

        // recyclerView에 adapter를 지정하여 그 adapter에 의해 항목이 구성되게 하고, 그 구성돼 있는 항목이 지정한 layoutManager에 의해서 나열되게끔...
        binding.recyclerView.layoutManager = layoutManager // 항목들이 세로 방향으로 쭉 나열되고
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(MyDecoration(activity as Context)) // 데코레이션까지 적용된다

        return binding.root

        // 여기까지 하면 첫 번째 fragment 화면을 recycler view에 의해서 구성시킨 것이 된다
        // 이제는 메뉴를 만들러 갑시다...
    }

}