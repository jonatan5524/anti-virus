rule Chacha_256_constant {
    meta:
		author = "spelissier"
		description = "Look for 256-bit key Chacha stream cipher constant"
		date = "2019-12"
		reference = "https://tools.ietf.org/html/rfc8439#page-8"
	strings:
		$c0 = "expand 32-byte k"
	condition:
		$c0
}