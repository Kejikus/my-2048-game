package com.kejikus.my2048game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

private const val ARG_VALUE = "value"
private const val ARG_STACK = "stack"

/**
 * A simple [Fragment] subclass.
 * Use the [TileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var value: Int? = null
    private var stack: Int? = null
    private var binding: Tile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            value = it.getInt(ARG_VALUE)
            stack = it.getInt(ARG_STACK)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tile, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_VALUE, param1)
                    putString(ARG_STACK, param2)
                }
            }
    }
}