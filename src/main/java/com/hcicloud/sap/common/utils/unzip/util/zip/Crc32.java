package com.hcicloud.sap.common.utils.unzip.util.zip;

/*    */ 
/*    */ public class Crc32
/*    */ {
/*  6 */   private static long[] crc_table = { 
/*  7 */     0, 1996959894L, 3993919788L, 2567524794L, 
/*  8 */     124634137L, 1886057615L, 3915621685L, 2657392035L, 
/*  9 */     249268274L, 2044508324L, 3772115230L, 2547177864L, 
/* 10 */     162941995L, 2125561021L, 3887607047L, 2428444049L, 
/* 11 */     498536548L, 1789927666L, 4089016648L, 2227061214L, 
/* 12 */     450548861L, 1843258603L, 4107580753L, 2211677639L, 
/* 13 */     325883990L, 1684777152L, 4251122042L, 2321926636L, 
/* 14 */     335633487L, 1661365465L, 4195302755L, 2366115317L, 
/* 15 */     997073096L, 1281953886L, 3579855332L, 2724688242L, 
/* 16 */     1006888145L, 1258607687L, 3524101629L, 2768942443L, 
/* 17 */     901097722L, 1119000684L, 3686517206L, 2898065728L, 
/* 18 */     853044451L, 1172266101L, 3705015759L, 2882616665L, 
/* 19 */     651767980L, 1373503546L, 3369554304L, 3218104598L, 
/* 20 */     565507253L, 1454621731L, 3485111705L, 3099436303L, 
/* 21 */     671266974L, 1594198024L, 3322730930L, 2970347812L, 
/* 22 */     795835527L, 1483230225L, 3244367275L, 3060149565L, 
/* 23 */     1994146192L, 31158534L, 2563907772L, 4023717930L, 
/* 24 */     1907459465L, 112637215L, 2680153253L, 3904427059L, 
/* 25 */     2013776290L, 251722036L, 2517215374L, 3775830040L, 
/* 26 */     2137656763L, 141376813L, 2439277719L, 3865271297L, 
/* 27 */     1802195444L, 476864866L, 2238001368L, 4066508878L, 
/* 28 */     1812370925L, 453092731L, 2181625025L, 4111451223L, 
/* 29 */     1706088902L, 314042704L, 2344532202L, 4240017532L, 
/* 30 */     1658658271L, 366619977L, 2362670323L, 4224994405L, 
/* 31 */     1303535960L, 984961486L, 2747007092L, 3569037538L, 
/* 32 */     1256170817L, 1037604311L, 2765210733L, 3554079995L, 
/* 33 */     1131014506L, 879679996L, 2909243462L, 3663771856L, 
/* 34 */     1141124467L, 855842277L, 2852801631L, 3708648649L, 
/* 35 */     1342533948L, 654459306L, 3188396048L, 3373015174L, 
/* 36 */     1466479909L, 544179635L, 3110523913L, 3462522015L, 
/* 37 */     1591671054L, 702138776L, 2966460450L, 3352799412L, 
/* 38 */     1504918807L, 783551873L, 3082640443L, 3233442989L, 
/* 39 */     3988292384L, 2596254646L, 62317068L, 1957810842L, 
/* 40 */     3939845945L, 2647816111L, 81470997L, 1943803523L, 
/* 41 */     3814918930L, 2489596804L, 225274430L, 2053790376L, 
/* 42 */     3826175755L, 2466906013L, 167816743L, 2097651377L, 
/* 43 */     4027552580L, 2265490386L, 503444072L, 1762050814L, 
/* 44 */     4150417245L, 2154129355L, 426522225L, 1852507879L, 
/* 45 */     4275313526L, 2312317920L, 282753626L, 1742555852L, 
/* 46 */     4189708143L, 2394877945L, 397917763L, 1622183637L, 
/* 47 */     3604390888L, 2714866558L, 953729732L, 1340076626L, 
/* 48 */     3518719985L, 2797360999L, 1068828381L, 1219638859L, 
/* 49 */     3624741850L, 2936675148L, 906185462L, 1090812512L, 
/* 50 */     3747672003L, 2825379669L, 829329135L, 1181335161L, 
/* 51 */     3412177804L, 3160834842L, 628085408L, 1382605366L, 
/* 52 */     3423369109L, 3138078467L, 570562233L, 1426400815L, 
/* 53 */     3317316542L, 2998733608L, 733239954L, 1555261956L, 
/* 54 */     3268935591L, 3050360625L, 752459403L, 1541320221L, 
/* 55 */     2607071920L, 3965973030L, 1969922972L, 40735498L, 
/* 56 */     2617837225L, 3943577151L, 1913087877L, 83908371L, 
/* 57 */     2512341634L, 3803740692L, 2075208622L, 213261112L, 
/* 58 */     2463272603L, 3855990285L, 2094854071L, 198958881L, 
/* 59 */     2262029012L, 4057260610L, 1759359992L, 534414190L, 
/* 60 */     2176718541L, 4139329115L, 1873836001L, 414664567L, 
/* 61 */     2282248934L, 4279200368L, 1711684554L, 285281116L, 
/* 62 */     2405801727L, 4167216745L, 1634467795L, 376229701L, 
/* 63 */     2685067896L, 3608007406L, 1308918612L, 956543938L, 
/* 64 */     2808555105L, 3495958263L, 1231636301L, 1047427035L, 
/* 65 */     2932959818L, 3654703836L, 1088359270L, 936918000L, 
/* 66 */     2847714899L, 3736837829L, 1202900863L, 817233897L, 
/* 67 */     3183342108L, 3401237130L, 1404277552L, 615818150L, 
/* 68 */     3134207493L, 3453421203L, 1423857449L, 601450431L, 
/* 69 */     3009837614L, 3294710456L, 1567103746L, 711928724L, 
/* 70 */     3020668471L, 3272380065L, 1510334235L, 755167117L };
/*    */ 
/*    */   public static long update(long pCrc32, int bval)
/*    */   {
/* 76 */     long c = pCrc32;
/* 77 */     c = crc_table[((int)(c ^ bval) & 0xFF)] ^ c >> 8;
/* 78 */     return c;
/*    */   }
/*    */ }

/* Location:           E:\移联百汇\二期设计\java压缩加密\win.jar
 * Qualified Name:     nochump.util.zip.Crc32
 * JD-Core Version:    0.5.3
 */