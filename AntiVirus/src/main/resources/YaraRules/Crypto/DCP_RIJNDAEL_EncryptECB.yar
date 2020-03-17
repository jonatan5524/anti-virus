rule DCP_RIJNDAEL_Init {
	meta:
		author = "_pusher_"
		description = "Look for DCP RijnDael Init"
		date = "2016-07"
	strings:
		$c0 = { 55 8B EC 51 53 56 57 89 4D FC 8B FA 8B D8 8B 75 08 56 8B D7 8B 4D FC 8B C3 E8 ?? ?? ?? ?? 8B D7 8B 4D FC 8B C3 8B 38 FF 57 ?? 85 F6 75 25 8D 43 38 33 C9 BA 10 00 00 00 E8 ?? ?? ?? ?? 8D 4B 38 8D 53 38 8B C3 8B 30 FF 56 ?? 8B C3 8B 10 FF 52 ?? EB 16 8D 53 38 8B C6 B9 10 00 00 00 E8 ?? ?? ?? ?? 8B C3 8B 10 FF 52 ?? 5F 5E 5B 59 5D C2 04 00 }
	condition:
		$c0
}