rule FGint_ConvertBase256to64
{	meta:
		author = "_pusher_"
		date = "2015-05"
		description = "FGint ConvertBase256to64"
	strings:
		$c0 = { 55 8B EC 81 C4 EC FB FF FF 53 56 57 33 C9 89 8D EC FB FF FF 89 8D F0 FB FF FF 89 4D F8 8B FA 89 45 FC B9 00 01 00 00 8D 85 F4 FB FF FF 8B 15 ?? ?? ?? ?? E8 ?? ?? ?? ?? 33 C0 55 68 ?? ?? ?? ?? 64 FF 30 64 89 20 8D 85 F4 FB FF FF BA FF 00 00 00 E8 ?? ?? ?? ?? 8D 45 F8 E8 ?? ?? ?? ?? 8B 45 FC E8 ?? ?? ?? ?? 8B D8 85 DB 7E 2F BE 01 00 00 00 8D 45 F8 8B 55 FC 0F B6 54 32 FF 8B 94 95 F4 FB FF FF E8 ?? ?? ?? ?? 46 4B 75 E5 EB }
	condition:
		$c0
}