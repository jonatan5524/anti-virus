rule CRC32_table {
	meta:
		author = "_pusher_"
		description = "Look for CRC32 table"
		date = "2015-05"
		version = "0.1"
	strings:
		$c0 = { 00 00 00 00 96 30 07 77 2C 61 0E EE BA 51 09 99 19 C4 6D 07 }
	condition:
		$c0
}