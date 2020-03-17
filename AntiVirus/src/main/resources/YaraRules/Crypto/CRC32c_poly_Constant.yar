rule CRC32c_poly_Constant {
	meta:
		author = "_pusher_"
		description = "Look for CRC32c (Castagnoli) [poly]"
		date = "2016-08"
	strings:
		$c0 = { 783BF682 }
	condition:
		$c0
}