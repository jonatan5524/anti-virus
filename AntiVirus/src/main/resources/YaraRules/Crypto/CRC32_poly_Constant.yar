rule CRC32_poly_Constant {
	meta:
		author = "_pusher_"
		description = "Look for CRC32 [poly]"
		date = "2015-05"
		version = "0.1"
	strings:
		$c0 = { 2083B8ED }
	condition:
		$c0
}