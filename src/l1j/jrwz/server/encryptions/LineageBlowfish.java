package l1j.jrwz.server.encryptions;

import l1j.jrwz.server.types.UChar8;
import l1j.jrwz.server.types.ULong32;

public class LineageBlowfish {

    private static int[] P = { 0x243F6A88, 0x85A308D3, 0x13198A2E, 0x3707344,
            0x0A4093822, 0x299F31D0, 0x82EFA98, 0xEC4E6C89, 0x452821E6,
            0x38D01377, 0x0BE5466CF, 0x34E90C6C, 0x0C0AC29B7, 0x0C97C50DD,
            0x3F84D5B5, 0x0B5470917, 0x9216D5D9, 0x8979FB1B };

    private static long[][] S = {
            { 0x0D1310BA6L, 0x98DFB5ACL, 0x2FFD72DBL, 0x0D01ADFB7L,
                    0x0B8E1AFEDL, 0x6A267E96L, 0x0BA7C9045L, 0x0F12C7F99L,
                    0x24A19947L, 0x0B3916CF7L, 0x801F2E2L, 0x858EFC16L,
                    0x636920D8L, 0x71574E69L, 0x0A458FEA3L, 0x0F4933D7EL,
                    0x0D95748FL, 0x728EB658L, 0x718BCD58L, 0x82154AEEL,
                    0x7B54A41DL, 0x0C25A59B5L, 0x9C30D539L, 0x2AF26013L,
                    0x0C5D1B023L, 0x286085F0L, 0x0CA417918L, 0x0B8DB38EFL,
                    0x8E79DCB0L, 0x603A180EL, 0x6C9E0E8BL, 0x0B01E8A3EL,
                    0x0D71577C1L, 0x0BD314B27L, 0x78AF2FDAL, 0x55605C60L,
                    0x0E65525F3L, 0x0AA55AB94L, 0x57489862L, 0x63E81440L,
                    0x55CA396AL, 0x2AAB10B6L, 0x0B4CC5C34L, 0x1141E8CEL,
                    0x0A15486AFL, 0x7C72E993L, 0x0B3EE1411L, 0x636FBC2AL,
                    0x2BA9C55DL, 0x741831F6L, 0x0CE5C3E16L, 0x9B87931EL,
                    0x0AFD6BA33L, 0x6C24CF5CL, 0x7A325381L, 0x28958677L,
                    0x3B8F4898L, 0x6B4BB9AFL, 0x0C4BFE81BL, 0x66282193L,
                    0x61D809CCL, 0x0FB21A991L, 0x487CAC60L, 0x5DEC8032L,
                    0x0EF845D5DL, 0x0E98575B1L, 0x0DC262302L, 0x0EB651B88L,
                    0x23893E81L, 0x0D396ACC5L, 0x0F6D6FF3L, 0x83F44239L,
                    0x2E0B4482L, 0x0A4842004L, 0x69C8F04AL, 0x9E1F9B5EL,
                    0x21C66842L, 0x0F6E96C9AL, 0x670C9C61L, 0x0ABD388F0L,
                    0x6A51A0D2L, 0x0D8542F68L, 0x960FA728L, 0x0AB5133A3L,
                    0x6EEF0B6CL, 0x137A3BE4L, 0x0BA3BF050L, 0x7EFB2A98L,
                    0x0A1F1651DL, 0x39AF0176L, 0x66CA593EL, 0x82430E88L,
                    0x8CEE8619L, 0x456F9FB4L, 0x7D84A5C3L, 0x3B8B5EBEL,
                    0x0E06F75D8L, 0x85C12073L, 0x401A449FL, 0x56C16AA6L,
                    0x4ED3AA62L, 0x363F7706L, 0x1BFEDF72L, 0x429B023DL,
                    0x37D0D724L, 0x0D00A1248L, 0x0DB0FEAD3L, 0x49F1C09BL,
                    0x75372C9L, 0x80991B7BL, 0x25D479D8L, 0x0F6E8DEF7L,
                    0x0E3FE501AL, 0x0B6794C3BL, 0x976CE0BDL, 0x4C006BAL,
                    0x0C1A94FB6L, 0x409F60C4L, 0x5E5C9EC2L, 0x196A2463L,
                    0x68FB6FAFL, 0x3E6C53B5L, 0x1339B2EBL, 0x3B52EC6FL,
                    0x6DFC511FL, 0x9B30952CL, 0x0CC814544L, 0x0AF5EBD09L,
                    0x0BEE3D004L, 0x0DE334AFDL, 0x660F2807L, 0x192E4BB3L,
                    0x0C0CBA857L, 0x45C8740FL, 0x0D20B5F39L, 0x0B9D3FBDBL,
                    0x5579C0BDL, 0x1A60320AL, 0x0D6A100C6L, 0x402C7279L,
                    0x679F25FEL, 0x0FB1FA3CCL, 0x8EA5E9F8L, 0x0DB3222F8L,
                    0x3C7516DFL, 0x0FD616B15L, 0x2F501EC8L, 0x0AD0552ABL,
                    0x323DB5FAL, 0x0FD238760L, 0x53317B48L, 0x3E00DF82L,
                    0x9E5C57BBL, 0x0CA6F8CA0L, 0x1A87562EL, 0x0DF1769DBL,
                    0x0D542A8F6L, 0x287EFFC3L, 0x0AC6732C6L, 0x8C4F5573L,
                    0x695B27B0L, 0x0BBCA58C8L, 0x0E1FFA35DL, 0x0B8F011A0L,
                    0x10FA3D98L, 0x0FD2183B8L, 0x4AFCB56CL, 0x2DD1D35BL,
                    0x9A53E479L, 0x0B6F84565L, 0x0D28E49BCL, 0x4BFB9790L,
                    0x0E1DDF2DAL, 0x0A4CB7E33L, 0x62FB1341L, 0x0CEE4C6E8L,
                    0x0EF20CADAL, 0x36774C01L, 0x0D07E9EFEL, 0x2BF11FB4L,
                    0x95DBDA4DL, 0x0AE909198L, 0x0EAAD8E71L, 0x6B93D5A0L,
                    0x0D08ED1D0L, 0x0AFC725E0L, 0x8E3C5B2FL, 0x8E7594B7L,
                    0x8FF6E2FBL, 0x0F2122B64L, 0x8888B812L, 0x900DF01CL,
                    0x4FAD5EA0L, 0x688FC31CL, 0x0D1CFF191L, 0x0B3A8C1ADL,
                    0x2F2F2218L, 0x0BE0E1777L, 0x0EA752DFEL, 0x8B021FA1L,
                    0x0E5A0CC0FL, 0x0B56F74E8L, 0x18ACF3D6L, 0x0CE89E299L,
                    0x0B4A84FE0L, 0x0FD13E0B7L, 0x7CC43B81L, 0x0D2ADA8D9L,
                    0x165FA266L, 0x80957705L, 0x93CC7314L, 0x211A1477L,
                    0x0E6AD2065L, 0x77B5FA86L, 0x0C75442F5L, 0x0FB9D35CFL,
                    0x0EBCDAF0CL, 0x7B3E89A0L, 0x0D6411BD3L, 0x0AE1E7E49L,
                    0x250E2DL, 0x2071B35EL, 0x226800BBL, 0x57B8E0AFL,
                    0x2464369BL, 0x0F009B91EL, 0x5563911DL, 0x59DFA6AAL,
                    0x78C14389L, 0x0D95A537FL, 0x207D5BA2L, 0x2E5B9C5L,
                    0x83260376L, 0x6295CFA9L, 0x11C81968L, 0x4E734A41L,
                    0x0B3472DCAL, 0x7B14A94AL, 0x1B510052L, 0x9A532915L,
                    0x0D60F573FL, 0x0BC9BC6E4L, 0x2B60A476L, 0x81E67400L,
                    0x8BA6FB5L, 0x571BE91FL, 0x0F296EC6BL, 0x2A0DD915L,
                    0x0B6636521L, 0x0E7B9F9B6L, 0x0FF34052EL, 0x0C5855664L,
                    0x53B02D5DL, 0x0A99F8FA1L, 0x8BA4799L, 0x6E85076AL },
            { 0x4B7A70E9L, 0x0B5B32944L, 0x0DB75092EL, 0x0C4192623L,
                    0x0AD6EA6B0L, 0x49A7DF7DL, 0x9CEE60B8L, 0x8FEDB266L,
                    0x0ECAA8C71L, 0x699A17FFL, 0x5664526CL, 0x0C2B19EE1L,
                    0x193602A5L, 0x75094C29L, 0x0A0591340L, 0x0E4183A3EL,
                    0x3F54989AL, 0x5B429D65L, 0x6B8FE4D6L, 0x99F73FD6L,
                    0x0A1D29C07L, 0x0EFE830F5L, 0x4D2D38E6L, 0x0F0255DC1L,
                    0x4CDD2086L, 0x8470EB26L, 0x6382E9C6L, 0x21ECC5EL,
                    0x9686B3FL, 0x3EBAEFC9L, 0x3C971814L, 0x6B6A70A1L,
                    0x687F3584L, 0x52A0E286L, 0x0B79C5305L, 0x0AA500737L,
                    0x3E07841CL, 0x7FDEAE5CL, 0x8E7D44ECL, 0x5716F2B8L,
                    0x0B03ADA37L, 0x0F0500C0DL, 0x0F01C1F04L, 0x200B3FFL,
                    0x0AE0CF51AL, 0x3CB574B2L, 0x25837A58L, 0x0DC0921BDL,
                    0x0D19113F9L, 0x7CA92FF6L, 0x94324773L, 0x22F54701L,
                    0x3AE5E581L, 0x37C2DADCL, 0x0C8B57634L, 0x9AF3DDA7L,
                    0x0A9446146L, 0x0FD0030EL, 0x0ECC8C73EL, 0x0A4751E41L,
                    0x0E238CD99L, 0x3BEA0E2FL, 0x3280BBA1L, 0x183EB331L,
                    0x4E548B38L, 0x4F6DB908L, 0x6F420D03L, 0x0F60A04BFL,
                    0x2CB81290L, 0x24977C79L, 0x5679B072L, 0x0BCAF89AFL,
                    0x0DE9A771FL, 0x0D9930810L, 0x0B38BAE12L, 0x0DCCF3F2EL,
                    0x5512721FL, 0x2E6B7124L, 0x501ADDE6L, 0x9F84CD87L,
                    0x7A584718L, 0x7408DA17L, 0x0BC9F9ABCL, 0x0E94B7D8CL,
                    0x0EC7AEC3AL, 0x0DB851DFAL, 0x63094366L, 0x0C464C3D2L,
                    0x0EF1C1847L, 0x3215D908L, 0x0DD433B37L, 0x24C2BA16L,
                    0x12A14D43L, 0x2A65C451L, 0x50940002L, 0x133AE4DDL,
                    0x71DFF89EL, 0x10314E55L, 0x81AC77D6L, 0x5F11199BL,
                    0x43556F1L, 0x0D7A3C76BL, 0x3C11183BL, 0x5924A509L,
                    0x0F28FE6EDL, 0x97F1FBFAL, 0x9EBABF2CL, 0x1E153C6EL,
                    0x86E34570L, 0x0EAE96FB1L, 0x860E5E0AL, 0x5A3E2AB3L,
                    0x771FE71CL, 0x4E3D06FAL, 0x2965DCB9L, 0x99E71D0FL,
                    0x803E89D6L, 0x5266C825L, 0x2E4CC978L, 0x9C10B36AL,
                    0x0C6150EBAL, 0x94E2EA78L, 0x0A5FC3C53L, 0x1E0A2DF4L,
                    0x0F2F74EA7L, 0x361D2B3DL, 0x1939260FL, 0x19C27960L,
                    0x5223A708L, 0x0F71312B6L, 0x0EBADFE6EL, 0x0EAC31F66L,
                    0x0E3BC4595L, 0x0A67BC883L, 0x0B17F37D1L, 0x18CFF28L,
                    0x0C332DDEFL, 0x0BE6C5AA5L, 0x65582185L, 0x68AB9802L,
                    0x0EECEA50FL, 0x0DB2F953BL, 0x2AEF7DADL, 0x5B6E2F84L,
                    0x1521B628L, 0x29076170L, 0x0ECDD4775L, 0x619F1510L,
                    0x13CCA830L, 0x0EB61BD96L, 0x334FE1EL, 0x0AA0363CFL,
                    0x0B5735C90L, 0x4C70A239L, 0x0D59E9E0BL, 0x0CBAADE14L,
                    0x0EECC86BCL, 0x60622CA7L, 0x9CAB5CABL, 0x0B2F3846EL,
                    0x648B1EAFL, 0x19BDF0CAL, 0x0A02369B9L, 0x655ABB50L,
                    0x40685A32L, 0x3C2AB4B3L, 0x319EE9D5L, 0x0C021B8F7L,
                    0x9B540B19L, 0x875FA099L, 0x95F7997EL, 0x623D7DA8L,
                    0x0F837889AL, 0x97E32D77L, 0x11ED935FL, 0x16681281L,
                    0x0E358829L, 0x0C7E61FD6L, 0x96DEDFA1L, 0x7858BA99L,
                    0x57F584A5L, 0x1B227263L, 0x9B83C3FFL, 0x1AC24696L,
                    0x0CDB30AEBL, 0x532E3054L, 0x8FD948E4L, 0x6DBC3128L,
                    0x58EBF2EFL, 0x34C6FFEAL, 0x0FE28ED61L, 0x0EE7C3C73L,
                    0x5D4A14D9L, 0x0E864B7E3L, 0x42105D14L, 0x203E13E0L,
                    0x45EEE2B6L, 0x0A3AAABEAL, 0x0DB6C4F15L, 0x0FACB4FD0L,
                    0x0C742F442L, 0x0EF6ABBB5L, 0x654F3B1DL, 0x41CD2105L,
                    0x0D81E799EL, 0x86854DC7L, 0x0E44B476AL, 0x3D816250L,
                    0x0CF62A1F2L, 0x5B8D2646L, 0x0FC8883A0L, 0x0C1C7B6A3L,
                    0x7F1524C3L, 0x69CB7492L, 0x47848A0BL, 0x5692B285L,
                    0x95BBF00L, 0x0AD19489DL, 0x1462B174L, 0x23820E00L,
                    0x58428D2AL, 0x0C55F5EAL, 0x1DADF43EL, 0x233F7061L,
                    0x3372F092L, 0x8D937E41L, 0x0D65FECF1L, 0x6C223BDBL,
                    0x7CDE3759L, 0x0CBEE7460L, 0x4085F2A7L, 0x0CE77326EL,
                    0x0A6078084L, 0x19F8509EL, 0x0E8EFD855L, 0x61D99735L,
                    0x0A969A7AAL, 0x0C50C06C2L, 0x5A04ABFCL, 0x800BCADCL,
                    0x9E447A2EL, 0x0C3453484L, 0x0FDD56705L, 0x0E1E9EC9L,
                    0x0DB73DBD3L, 0x105588CDL, 0x675FDA79L, 0x0E3674340L,
                    0x0C5C43465L, 0x713E38D8L, 0x3D28F89EL, 0x0F16DFF20L,
                    0x153E21E7L, 0x8FB03D4AL, 0x0E6E39F2BL, 0x0DB83ADF7L },
            { 0x0E93D5A68L, 0x948140F7L, 0x0F64C261CL, 0x94692934L,
                    0x411520F7L, 0x7602D4F7L, 0x0BCF46B2EL, 0x0D4A20068L,
                    0x0D4082471L, 0x3320F46AL, 0x43B7D4B7L, 0x500061AFL,
                    0x1E39F62EL, 0x97244546L, 0x14214F74L, 0x0BF8B8840L,
                    0x4D95FC1DL, 0x96B591AFL, 0x70F4DDD3L, 0x66A02F45L,
                    0x0BFBC09ECL, 0x3BD9785L, 0x7FAC6DD0L, 0x31CB8504L,
                    0x96EB27B3L, 0x55FD3941L, 0x0DA2547E6L, 0x0ABCA0A9AL,
                    0x28507825L, 0x530429F4L, 0x0A2C86DAL, 0x0E9B66DFBL,
                    0x68DC1462L, 0x0D7486900L, 0x680EC0A4L, 0x27A18DEEL,
                    0x4F3FFEA2L, 0x0E887AD8CL, 0x0B58CE006L, 0x7AF4D6B6L,
                    0x0AACE1E7CL, 0x0D3375FECL, 0x0CE78A399L, 0x406B2A42L,
                    0x20FE9E35L, 0x0D9F385B9L, 0x0EE39D7ABL, 0x3B124E8BL,
                    0x1DC9FAF7L, 0x4B6D1856L, 0x26A36631L, 0x0EAE397B2L,
                    0x3A6EFA74L, 0x0DD5B4332L, 0x6841E7F7L, 0x0CA7820FBL,
                    0x0FB0AF54EL, 0x0D8FEB397L, 0x454056ACL, 0x0BA489527L,
                    0x55533A3AL, 0x20838D87L, 0x0FE6BA9B7L, 0x0D096954BL,
                    0x55A867BCL, 0x0A1159A58L, 0x0CCA92963L, 0x99E1DB33L,
                    0x0A62A4A56L, 0x3F3125F9L, 0x5EF47E1CL, 0x9029317CL,
                    0x0FDF8E802L, 0x4272F70L, 0x80BB155CL, 0x5282CE3L,
                    0x95C11548L, 0x0E4C66D22L, 0x48C1133FL, 0x0C70F86DCL,
                    0x7F9C9EEL, 0x41041F0FL, 0x404779A4L, 0x5D886E17L,
                    0x325F51EBL, 0x0D59BC0D1L, 0x0F2BCC18FL, 0x41113564L,
                    0x257B7834L, 0x602A9C60L, 0x0DFF8E8A3L, 0x1F636C1BL,
                    0x0E12B4C2L, 0x2E1329EL, 0x0AF664FD1L, 0x0CAD18115L,
                    0x6B2395E0L, 0x333E92E1L, 0x3B240B62L, 0x0EEBEB922L,
                    0x85B2A20EL, 0x0E6BA0D99L, 0x0DE720C8CL, 0x2DA2F728L,
                    0x0D0127845L, 0x95B794FDL, 0x647D0862L, 0x0E7CCF5F0L,
                    0x5449A36FL, 0x877D48FAL, 0x0C39DFD27L, 0x0F33E8D1EL,
                    0x0A476341L, 0x992EFF74L, 0x3A6F6EABL, 0x0F4F8FD37L,
                    0x0A812DC60L, 0x0A1EBDDF8L, 0x991BE14CL, 0x0DB6E6B0DL,
                    0x0C67B5510L, 0x6D672C37L, 0x2765D43BL, 0x0DCD0E804L,
                    0x0F1290DC7L, 0x0CC00FFA3L, 0x0B5390F92L, 0x690FED0BL,
                    0x667B9FFBL, 0x0CEDB7D9CL, 0x0A091CF0BL, 0x0D9155EA3L,
                    0x0BB132F88L, 0x515BAD24L, 0x7B9479BFL, 0x763BD6EBL,
                    0x37392EB3L, 0x0CC115979L, 0x8026E297L, 0x0F42E312DL,
                    0x6842ADA7L, 0x0C66A2B3BL, 0x12754CCCL, 0x782EF11CL,
                    0x6A124237L, 0x0B79251E7L, 0x6A1BBE6L, 0x4BFB6350L,
                    0x1A6B1018L, 0x11CAEDFAL, 0x3D25BDD8L, 0x0E2E1C3C9L,
                    0x44421659L, 0x0A121386L, 0x0D90CEC6EL, 0x0D5ABEA2AL,
                    0x64AF674EL, 0x0DA86A85FL, 0x0BEBFE988L, 0x64E4C3FEL,
                    0x9DBC8057L, 0x0F0F7C086L, 0x60787BF8L, 0x6003604DL,
                    0x0D1FD8346L, 0x0F6381FB0L, 0x7745AE04L, 0x0D736FCCCL,
                    0x83426B33L, 0x0F01EAB71L, 0x0B0804187L, 0x3C005E5FL,
                    0x77A057BEL, 0x0BDE8AE24L, 0x55464299L, 0x0BF582E61L,
                    0x4E58F48FL, 0x0F2DDFDA2L, 0x0F474EF38L, 0x8789BDC2L,
                    0x5366F9C3L, 0x0C8B38E74L, 0x0B475F255L, 0x46FCD9B9L,
                    0x7AEB2661L, 0x8B1DDF84L, 0x846A0E79L, 0x915F95E2L,
                    0x466E598EL, 0x20B45770L, 0x8CD55591L, 0x0C902DE4CL,
                    0x0B90BACE1L, 0x0BB8205D0L, 0x11A86248L, 0x7574A99EL,
                    0x0B77F19B6L, 0x0E0A9DC09L, 0x662D09A1L, 0x0C4324633L,
                    0x0E85A1F02L, 0x9F0BE8CL, 0x4A99A025L, 0x1D6EFE10L,
                    0x1AB93D1DL, 0x0BA5A4DFL, 0x0A186F20FL, 0x2868F169L,
                    0x0DCB7DA83L, 0x573906FEL, 0x0A1E2CE9BL, 0x4FCD7F52L,
                    0x50115E01L, 0x0A70683FAL, 0x0A002B5C4L, 0x0DE6D027L,
                    0x9AF88C27L, 0x773F8641L, 0x0C3604C06L, 0x61A806B5L,
                    0x0F0177A28L, 0x0C0F586E0L, 0x6058AAL, 0x30DC7D62L,
                    0x11E69ED7L, 0x2338EA63L, 0x53C2DD94L, 0x0C2C21634L,
                    0x0BBCBEE56L, 0x90BCB6DEL, 0x0EBFC7DA1L, 0x0CE591D76L,
                    0x6F05E409L, 0x4B7C0188L, 0x39720A3DL, 0x7C927C24L,
                    0x86E3725FL, 0x724D9DB9L, 0x1AC15BB4L, 0x0D39EB8FCL,
                    0x0ED545578L, 0x8FCA5B5L, 0x0D83D7CD3L, 0x4DAD0FC4L,
                    0x1E50EF5EL, 0x0B161E6F8L, 0x0A28514D9L, 0x6C51133CL,
                    0x6FD5C7E7L, 0x56E14EC4L, 0x362ABFCEL, 0x0DDC6C837L,
                    0x0D79A3234L, 0x92638212L, 0x670EFA8EL, 0x406000E0L },
            { 0x3A39CE37L, 0x0D3FAF5CFL, 0x0ABC27737L, 0x5AC52D1BL,
                    0x5CB0679EL, 0x4FA33742L, 0x0D3822740L, 0x99BC9BBEL,
                    0x0D5118E9DL, 0x0BF0F7315L, 0x0D62D1C7EL, 0x0C700C47BL,
                    0x0B78C1B6BL, 0x21A19045L, 0x0B26EB1BEL, 0x6A366EB4L,
                    0x5748AB2FL, 0x0BC946E79L, 0x0C6A376D2L, 0x6549C2C8L,
                    0x530FF8EEL, 0x468DDE7DL, 0x0D5730A1DL, 0x4CD04DC6L,
                    0x2939BBDBL, 0x0A9BA4650L, 0x0AC9526E8L, 0x0BE5EE304L,
                    0x0A1FAD5F0L, 0x6A2D519AL, 0x63EF8CE2L, 0x9A86EE22L,
                    0x0C089C2B8L, 0x43242EF6L, 0x0A51E03AAL, 0x9CF2D0A4L,
                    0x83C061BAL, 0x9BE96A4DL, 0x8FE51550L, 0x0BA645BD6L,
                    0x2826A2F9L, 0x0A73A3AE1L, 0x4BA99586L, 0x0EF5562E9L,
                    0x0C72FEFD3L, 0x0F752F7DAL, 0x3F046F69L, 0x77FA0A59L,
                    0x80E4A915L, 0x87B08601L, 0x9B09E6ADL, 0x3B3EE593L,
                    0x0E990FD5AL, 0x9E34D797L, 0x2CF0B7D9L, 0x22B8B51L,
                    0x96D5AC3AL, 0x17DA67DL, 0x0D1CF3ED6L, 0x7C7D2D28L,
                    0x1F9F25CFL, 0x0ADF2B89BL, 0x5AD6B472L, 0x5A88F54CL,
                    0x0E029AC71L, 0x0E019A5E6L, 0x47B0ACFDL, 0x0ED93FA9BL,
                    0x0E8D3C48DL, 0x283B57CCL, 0x0F8D56629L, 0x79132E28L,
                    0x785F0191L, 0x0ED756055L, 0x0F7960E44L, 0x0E3D35E8CL,
                    0x15056DD4L, 0x88F46DBAL, 0x3A16125L, 0x564F0BDL,
                    0x0C3EB9E15L, 0x3C9057A2L, 0x97271AECL, 0x0A93A072AL,
                    0x1B3F6D9BL, 0x1E6321F5L, 0x0F59C66FBL, 0x26DCF319L,
                    0x7533D928L, 0x0B155FDF5L, 0x3563482L, 0x8ABA3CBBL,
                    0x28517711L, 0x0C20AD9F8L, 0x0ABCC5167L, 0x0CCAD925FL,
                    0x4DE81751L, 0x3830DC8EL, 0x379D5862L, 0x9320F991L,
                    0x0EA7A90C2L, 0x0FB3E7BCEL, 0x5121CE64L, 0x774FBE32L,
                    0x0A8B6E37EL, 0x0C3293D46L, 0x48DE5369L, 0x6413E680L,
                    0x0A2AE0810L, 0x0DD6DB224L, 0x69852DFDL, 0x9072166L,
                    0x0B39A460AL, 0x6445C0DDL, 0x586CDECFL, 0x1C20C8AEL,
                    0x5BBEF7DDL, 0x1B588D40L, 0x0CCD2017FL, 0x6BB4E3BBL,
                    0x0DDA26A7EL, 0x3A59FF45L, 0x3E350A44L, 0x0BCB4CDD5L,
                    0x72EACEA8L, 0x0FA6484BBL, 0x8D6612AEL, 0x0BF3C6F47L,
                    0x0D29BE463L, 0x542F5D9EL, 0x0AEC2771BL, 0x0F64E6370L,
                    0x740E0D8DL, 0x0E75B1357L, 0x0F8721671L, 0x0AF537D5DL,
                    0x4040CB08L, 0x4EB4E2CCL, 0x34D2466AL, 0x115AF84L,
                    0x0E1B00428L, 0x95983A1DL, 0x6B89FB4L, 0x0CE6EA048L,
                    0x6F3F3B82L, 0x3520AB82L, 0x11A1D4BL, 0x277227F8L,
                    0x611560B1L, 0x0E7933FDCL, 0x0BB3A792BL, 0x344525BDL,
                    0x0A08839E1L, 0x51CE794BL, 0x2F32C9B7L, 0x0A01FBAC9L,
                    0x0E01CC87EL, 0x0BCC7D1F6L, 0x0CF0111C3L, 0x0A1E8AAC7L,
                    0x1A908749L, 0x0D44FBD9AL, 0x0D0DADECBL, 0x0D50ADA38L,
                    0x339C32AL, 0x0C6913667L, 0x8DF9317CL, 0x0E0B12B4FL,
                    0x0F79E59B7L, 0x43F5BB3AL, 0x0F2D519FFL, 0x27D9459CL,
                    0x0BF97222CL, 0x15E6FC2AL, 0x0F91FC71L, 0x9B941525L,
                    0x0FAE59361L, 0x0CEB69CEBL, 0x0C2A86459L, 0x12BAA8D1L,
                    0x0B6C1075EL, 0x0E3056A0CL, 0x10D25065L, 0x0CB03A442L,
                    0x0E0EC6E0EL, 0x1698DB3BL, 0x4C98A0BEL, 0x3278E964L,
                    0x9F1F9532L, 0x0E0D392DFL, 0x0D3A0342BL, 0x8971F21EL,
                    0x1B0A7441L, 0x4BA3348CL, 0x0C5BE7120L, 0x0C37632D8L,
                    0x0DF359F8DL, 0x9B992F2EL, 0x0E60B6F47L, 0x0FE3F11DL,
                    0x0E54CDA54L, 0x1EDAD891L, 0x0CE6279CFL, 0x0CD3E7E6FL,
                    0x1618B166L, 0x0FD2C1D05L, 0x848FD2C5L, 0x0F6FB2299L,
                    0x0F523F357L, 0x0A6327623L, 0x93A83531L, 0x56CCCD02L,
                    0x0ACF08162L, 0x5A75EBB5L, 0x6E163697L, 0x88D273CCL,
                    0x0DE966292L, 0x81B949D0L, 0x4C50901BL, 0x71C65614L,
                    0x0E6C6C7BDL, 0x327A140AL, 0x45E1D006L, 0x0C3F27B9AL,
                    0x0C9AA53FDL, 0x62A80F00L, 0x0BB25BFE2L, 0x35BDD2F6L,
                    0x71126905L, 0x0B2040222L, 0x0B6CBCF7CL, 0x0CD769C2BL,
                    0x53113EC0L, 0x1640E3D3L, 0x38ABBD60L, 0x2547ADF0L,
                    0x0BA38209CL, 0x0F746CE76L, 0x77AFA1C5L, 0x20756060L,
                    0x85CBFE4EL, 0x8AE88DD8L, 0x7AAAF9B0L, 0x4CF9AA7EL,
                    0x1948C25CL, 0x2FB8A8CL, 0x1C36AE4L, 0x0D6EBE1F9L,
                    0x90D4F869L, 0x0A65CDEA0L, 0x3F09252DL, 0x0C208E69FL,
                    0x0B74E6132L, 0x0CE77E25BL, 0x578FDFE3L, 0x3AC372E6L } };

    static {
        for (int i = 0; i < S.length; ++i) {
            for (int j = 0; j < S[i].length; ++j) {
                S[i][j] = ULong32.fromLong64(S[i][j]);
            }
        }
    }

    private static int PSIZE = 18;// P.length;

    private static int SSIZE = 256;

    private static long v = 0, w = 0;

    public static int _rotr(int value, int n) {
        return (value >>> n) | (value << (32 - n));
    }

    public static void blowfish_encrypt(long L, long R) {
        long temp, xL = L, xR = R;

        for (int i = 0; i < 16; i++) {

            xL ^= P[i];
            xL = ULong32.fromLong64(xL);

            xR ^= blowfish_F(ULong32.fromLong64(xL));
            xR = ULong32.fromLong64(xR);

            temp = xL;
            xL = xR;
            xR = temp;
        }

        temp = xL;
        xL = xR;
        xR = temp;

        R = xR ^ P[16];
        L = xL ^ P[17];

        R = ULong32.fromLong64(R);
        L = ULong32.fromLong64(L);

        v = L;
        w = R;
    }

    public static long blowfish_F(long x) {
        char[] c = UChar8.fromULong32(x);

        int rtmp = (int) S[0][c[3]] + (int) S[1][c[2]];
        long rtmp2 = rtmp ^ S[2][c[1]];
        int rtmp3 = (int) rtmp2 + (int) S[3][c[0]];

        return rtmp3;
    }

    public static long[] blowfish_init(long[] keys) {
        int i = 0;
        int j = 0;

        char[] c = UChar8.fromArray(keys);

        for (i = 0; i < PSIZE; i++) {

            long k = 0L;
            if ((i & 1) != 0) {
                k = ((c[4] & 0xFF) << 24) | ((c[5] & 0xff) << 16)
                        | ((c[6] & 0xff) << 8) | (c[7] & 0xff);
            } else {
                k = ((c[0] & 0xFF) << 24) | ((c[1] & 0xff) << 16)
                        | ((c[2] & 0xff) << 8) | (c[3] & 0xff);
            }

            P[i] ^= k;
        }

        i = 0;
        for (j = 0; j < PSIZE / 2; j++, i += 2) {
            blowfish_encrypt(v, w);

            P[i] = (int) v;
            P[i + 1] = (int) w;
        }

        v = ULong32.fromInt32((int) v);
        w = ULong32.fromInt32((int) w);
        keys = null;

        i = 0;
        int index = 0;
        int index2 = i;
        for (j = 0; j < SSIZE * 2; j++, i += 2, index2 += 2) {
            blowfish_encrypt(v, w);

            if (i == 256) {
                index = 1;
                index2 = 0;
            }
            if (i == 512) {
                index = 2;
                index2 = 0;
            }
            if (i == 768) {
                index = 3;
                index2 = 0;
            }

            S[index][index2] = ULong32.fromInt32((int) v);
            S[index][index2 + 1] = ULong32.fromInt32((int) w);
        }

        v = 0;
        w = 0;
        return keys;
    }

    public static long[] getSeeds(long[] keys) {
        blowfish_init(keys);

        long rotrParam = ULong32.fromLong64(keys[0] ^ 0x9C30D539);
        keys[0] = ULong32.fromLong64(_rotr((int) rotrParam, 13));

        keys[1] = (keys[1] ^ keys[0] ^ 0x7C72E993);
        return keys;
    }

    void blowfish_decrypt(long L, long R) {
        long temp, xL = L, xR = R;

        for (int i = 17; i >= 2; i--) {

            xL ^= P[i];
            xR ^= blowfish_F(xL);

            temp = xL;
            xL = xR;
            xR = temp;
        }

        temp = xL;
        xL = xR;
        xR = temp;

        R = xR ^ P[1];
        L = xL ^ P[0];
    }

}
