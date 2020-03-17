rule CRC32b_poly_Constant {
	meta:
		author = "_pusher_"
		description = "Look for CRC32b [poly]"
		date = "2016-04"
		version = "0.1"
	strings:
		$c0 = { B71DC104 }
	condition:
		$c0
}