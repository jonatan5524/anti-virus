rule FGint_Base256StringToGInt
{	meta:
		author = "_pusher_"
		date = "2015-05"
		description = "FGint Base256StringToGInt"
	strings:
		$c0 = { 55 8B EC 81 C4 F8 FB FF FF 53 56 57 33 C9 89 4D F8 8B FA 89 45 FC 8B 45 FC ?? ?? ?? ?? ?? B9 00 01 00 00 8D 85 F8 FB FF FF 8B 15 ?? ?? ?? ?? ?? ?? ?? ?? ?? 33 C0 55 ?? ?? ?? ?? ?? 64 FF 30 64 89 20 8D 45 F8 ?? ?? ?? ?? ?? 8D 85 F8 FB FF FF BA FF 00 00 00 ?? ?? ?? ?? ?? 8B 45 FC ?? ?? ?? ?? ?? 8B D8 85 DB 7E 34 BE 01 00 00 00 8D 45 F8 8B 55 FC 0F B6 54 32 FF 8B 94 95 F8 FB FF FF ?? ?? ?? ?? ?? 46 4B 75 E5 EB 12 8D 45 F8 B9 01 00 00 00 BA 01 00 00 00 ?? ?? ?? ?? ?? 8B 45 F8 80 38 30 75 0F }
	condition:
		$c0
}