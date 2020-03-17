rule VC8_Random {
	meta:
		author = "_pusher_"
		description = "Look for Random function"
		date = "2016-01"
		version = "0.1"
	strings:
		$c0 = { E8 ?? ?? ?? ?? 8B 48 14 69 C9 FD 43 03 00 81 C1 C3 9E 26 00 89 48 14 8B C1 C1 E8 10 25 FF 7F 00 00 C3 }
	condition:
		$c0
}