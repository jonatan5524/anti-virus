rule Delphi_RandomRange {
	meta:
		author = "_pusher_"
		description = "Look for RandomRange function"
		date = "2016-06"
		version = "0.1"
	strings:
		$c0 = { 56 8B F2 8B D8 3B F3 7D 0E 8B C3 2B C6 E8 ?? ?? ?? ?? 03 C6 5E 5B C3 8B C6 2B C3 E8 ?? ?? ?? ?? 03 C3 5E 5B C3 }
	condition:
		$c0
}