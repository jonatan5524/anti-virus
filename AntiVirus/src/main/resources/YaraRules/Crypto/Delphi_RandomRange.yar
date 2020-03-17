rule Delphi_Random {
	meta:
		author = "_pusher_"
		description = "Look for Random function"
		date = "2015-08"
		version = "0.1"
	strings:
		$c0 = { 53 31 DB 69 93 ?? ?? ?? ?? 05 84 08 08 42 89 93 ?? ?? ?? ?? F7 E2 89 D0 5B C3 }
		//x64 rad
		$c1 = { 8B 05 ?? ?? ?? ?? 69 C0 05 84 08 08 83 C0 01 89 05 ?? ?? ?? ?? 8B C9 8B C0 48 0F AF C8 48 C1 E9 20 89 C8 C3 }
	condition:
		any of them
}