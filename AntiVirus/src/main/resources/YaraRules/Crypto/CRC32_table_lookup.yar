rule CRC32_table_lookup {
	meta:
		author = "_pusher_"
		description = "CRC32 table lookup"
		date = "2015-06"
		version = "0.1"
	strings:
		$c0 = { 8B 54 24 08 85 D2 7F 03 33 C0 C3 83 C8 FF 33 C9 85 D2 7E 29 56 8B 74 24 08 57 8D 9B 00 00 00 00 0F B6 3C 31 33 F8 81 E7 FF 00 00 00 C1 E8 08 33 04 BD ?? ?? ?? ?? 41 3B CA 7C E5 5F 5E F7 D0 C3 }
	condition:
		$c0
}