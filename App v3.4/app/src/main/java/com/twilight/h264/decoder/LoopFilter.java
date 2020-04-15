package com.twilight.h264.decoder;

public class LoopFilter {

	/* Deblocking filter (p153) */
//	public static final int[] alpha_table[52*3] = {
	public static final int[] alpha_table = {
	     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	     0,  0,  0,  0,  0,  0,  4,  4,  5,  6,
	     7,  8,  9, 10, 12, 13, 15, 17, 20, 22,
	    25, 28, 32, 36, 40, 45, 50, 56, 63, 71,
	    80, 90,101,113,127,144,162,182,203,226,
	   255,255,
	   255,255,255,255,255,255,255,255,255,255,255,255,255,
	   255,255,255,255,255,255,255,255,255,255,255,255,255,
	   255,255,255,255,255,255,255,255,255,255,255,255,255,
	   255,255,255,255,255,255,255,255,255,255,255,255,255,
	};
//	public static final int[] beta_table[52*3] = {
	public static final int[] beta_table = {
	     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	     0,  0,  0,  0,  0,  0,  2,  2,  2,  3,
	     3,  3,  3,  4,  4,  4,  6,  6,  7,  7,
	     8,  8,  9,  9, 10, 10, 11, 11, 12, 12,
	    13, 13, 14, 14, 15, 15, 16, 16, 17, 17,
	    18, 18,
	    18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18,
	    18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18,
	    18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18,
	    18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18,
	};
//	public static final int[] tc0_table[52*3][4] = {
	public static final int[][] tc0_table = {
	    {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 },
	    {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 },
	    {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 },
	    {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 },
	    {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 },
	    {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 },
	    {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 },
	    {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 },
	    {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 },
	    {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 },
	    {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 },
	    {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 0 }, {-1, 0, 0, 1 },
	    {-1, 0, 0, 1 }, {-1, 0, 0, 1 }, {-1, 0, 0, 1 }, {-1, 0, 1, 1 }, {-1, 0, 1, 1 }, {-1, 1, 1, 1 },
	    {-1, 1, 1, 1 }, {-1, 1, 1, 1 }, {-1, 1, 1, 1 }, {-1, 1, 1, 2 }, {-1, 1, 1, 2 }, {-1, 1, 1, 2 },
	    {-1, 1, 1, 2 }, {-1, 1, 2, 3 }, {-1, 1, 2, 3 }, {-1, 2, 2, 3 }, {-1, 2, 2, 4 }, {-1, 2, 3, 4 },
	    {-1, 2, 3, 4 }, {-1, 3, 3, 5 }, {-1, 3, 4, 6 }, {-1, 3, 4, 6 }, {-1, 4, 5, 7 }, {-1, 4, 5, 8 },
	    {-1, 4, 6, 9 }, {-1, 5, 7,10 }, {-1, 6, 8,11 }, {-1, 6, 8,13 }, {-1, 7,10,14 }, {-1, 8,11,16 },
	    {-1, 9,12,18 }, {-1,10,13,20 }, {-1,11,15,23 }, {-1,13,17,25 },
	    {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 },
	    {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 },
	    {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 },
	    {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 },
	    {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 },
	    {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 },
	    {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 },
	    {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 },
	    {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 }, {-1,13,17,25 },
	};
	
}
