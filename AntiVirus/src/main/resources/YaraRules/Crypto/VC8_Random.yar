rule VC6_Random {
	meta:
		author = "_pusher_"
		description = "Look for Random function"
		date = "2016-02"
	strings:
		$c0 = { A1 ?? ?? ?? ?? 69 C0 FD 43 03 00 05 C3 9E 26 00 A3 ?? ?? ?? ?? C1 F8 10 25 FF 7F 00 00 C3 }
	condition:
		$c0
}