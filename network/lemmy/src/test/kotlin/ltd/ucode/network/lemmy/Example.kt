package ltd.ucode.network.lemmy

import org.jsoup.Jsoup
import org.junit.Test

class Example {

    @Test
    fun testParse() {
        fun String.check() {
            Jsoup.parse(this).root().children().forEach { it.children().forEach { it.nodeName().let(::println) } }
        }

        ("<html><body></body></html>").check()
        ("<html/>").check()
        ("").check()
        ("""
            {"json": ["y", "e", "p"], "ok": 1}
        """.trimIndent()).check()

        val testHtml = listOf(
            """
                <!DOCTYPE html> <html> <head> <meta charset="UTF-8" /> <meta name="robots" content="noindex,nofollow,noarchive" /> <title>An Error Occurred: Not Found</title> <style>body { background-color: #fff; color: #222; font: 16px/1.5 -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; margin: 0; } .container { margin: 30px; max-width: 600px; } h1 { color: #dc3545; font-size: 24px; } h2 { font-size: 18px; }</style> </head> <body> <div class="container"> <h1>Oops! An Error Occurred</h1> <h2>The server returned a "404 Not Found".</h2> <p> Something is broken. Please let us know what you were doing when this error occurred. We will fix it as soon as possible. Sorry for any inconvenience caused. </p> </div> <script>(function(){var js = "window['__CF${"\$cv\$params"}']={r:'7e07d095aa580778',m:'D7WneYfng7HhrYhfF5FkAl4TQLuxoqfVMOVQfmiosjQ-1688310618-0-AZVIMOwVlPV++zUJetm1UitopmZYYhlp0OX72es8x1uE'};_cpo=document.createElement('script');_cpo.nonce='',_cpo.src='/cdn-cgi/challenge-platform/scripts/invisible.js',document.getElementsByTagName('head')[0].appendChild(_cpo);";var _0xh = document.createElement('iframe');_0xh.height = 1;_0xh.width = 1;_0xh.style.position = 'absolute';_0xh.style.top = 0;_0xh.style.left = 0;_0xh.style.border = 'none';_0xh.style.visibility = 'hidden';document.body.appendChild(_0xh);function handler() {var _0xi = _0xh.contentDocument || _0xh.contentWindow.document;if (_0xi) {var _0xj = _0xi.createElement('script');_0xj.nonce = '';_0xj.innerHTML = js;_0xi.getElementsByTagName('head')[0].appendChild(_0xj);}}if (document.readyState !== 'loading') {handler();} else if (window.addEventListener) {document.addEventListener('DOMContentLoaded', handler);} else {var prev = document.onreadystatechange || function () {};document.onreadystatechange = function (e) {prev(e);if (document.readyState !== 'loading') {document.onreadystatechange = prev;handler();}};}})();</script></body> </html>
            """.trimIndent(),
            """
                <!DOCTYPE html> <html lang="en-US"> <head> <title>Just a moment...</title> <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> <meta http-equiv="X-UA-Compatible" content="IE=Edge"> <meta name="robots" content="noindex,nofollow"> <meta name="viewport" content="width=device-width,initial-scale=1"> <link href="/cdn-cgi/styles/challenges.css" rel="stylesheet"> </head> <body class="no-js"> <div class="main-wrapper" role="main"> <div class="main-content"> <noscript> <div id="challenge-error-title"> <div class="h2"> <span class="icon-wrapper"> <div class="heading-icon warning-icon"></div> </span> <span id="challenge-error-text"> Enable JavaScript and cookies to continue </span> </div> </div> </noscript> <div id="trk_jschal_js" style="display:none;background-image:url('/cdn-cgi/images/trace/managed/nojs/transparent.gif?ray=7e01ac6c0e082bf0')"></div> <form id="challenge-form" action="/api/v3/user/login?__cf_chl_f_tk=Szj2ardQ30.b0BUlYIuAfNYgXp3GwR.Al4jFLbPMTh8-1688246222-0-gaNycGzNCLs" method="POST" enctype="application/x-www-form-urlencoded"> <input type="hidden" name="md" value="zgh4k3m_mHyanxlt5TFpYeINyyUD5Y7gyjvyYT_cnqQ-1688246222-0-AYsH_C_d8GPBYZ-4rCGR1ezCgXrTwzff10wNcFCM6ky7zSsdp7spAQZpCHL0qBALZokgShoCZ-K8eSlLdpQuvQ6L1_yXIjqZU3mgc1wpLalWmpZHqkpZPst886WlQs1v9_ELX7XXWRODvIEb3qsrFR1wvh0SGANSXCFIaNxAqE3wF6cRzVJWPjFMtztgVUZAGJFVg_99NJN9zzLCwbbLnoapbeVcR-e_-tSApbn46jQsqb8YGFnpc2CE2mZjtYZWVQ_NHMJjDs0hDfCeNRajNbTHg37ShGu0U8-NE6n18TXyj4aKS63ryrXzmu7da41BR7vFKdeTZB0E3bKyd359NMCPsX0QSXgc_ox-OFg27Qfc_ACEAR6Gvu-tmJdEvsJd5oKN9tI2eQB0TplpAYqmQGbdnvh9-ctZuAG-65PeArSvLe5NmCo-UyXaJ0VHf3qWp0_m5ZEk0G7Z1n9Gxx6x2VYQ0qstjrgQPBxFFjQCtdR0D9b0SIaCHQvl1JwX-FTYMV0ZTxoRKXfhSpNX21fTGK6JcDbw3_S1LYa2QI4DmTN9c0mYhnqUJWHC3_xdg7dbRPHOT6PhioCrroQ9sScPBdtQZ2JA-2Dxo-BV4MjnJZ5o0jugZx1JWl1GuXC9jXaHlvckuFtQEkETFnYBAoobJbw8wpSPTwI21hLrkLpa20XRU9Z5xLTMKF5YozakY7Kbd6cMdSqc-Z9NCQPLpkwvhoj6o0YnnmRZzKx08qzvHGuji7E3pQlRqbwcgfMr443HOXSHCu4d3k2NDr0-IohlwYcPL3joIRK1epEvM4gwEsYeuSIHqbNkU5M579aPNk9b9pdtEVJ-POOCjApGN4Kpfb4NK6tXaEbn1-9Rsqrilb25ndmNHNgrZunzBhpuVNsdEeApno1hwnPGOMBxu7bbq4SA13jo1dlXZy1L1SS8uQhBdDGq9LOSXIIWBAT3q5yeOhIjPA7ejpo1FCE1ahuAuDUggE_UnMHo0kUQoZUtHNFQmwKPhP28hX53rIOBYDOEDLNrwZhou8sm7akLZ9PYwcGbW9qCVoDYUtx_iHlaFKyT6zuONJs8ioqByLoY5pKeBJwYfgBk-5DSWUjH1y-fsUtygs5juL7gyetSxXBgrFJLLWAUAbsOhkDu0UtZrdtsqvvb_fXpA5XZnJrVAC8wXN6MxYxrfyxDPY-u45UnWADB6HAlYLTxg9DuE0HABwska34IIIYV5dXkvHOTJ7TdoQ30pSrgL40NwWEZvDUA0Zku8F90GpsJOXdux8VajU1Ker-uWv-uwc8jvx2CZdJcmpIqp0y4F7LDe5IAl1D3FJlMpEeaN5qsRRA-NX0f8xk3_Oue00Aq7eJYCbO0sn3Ej0QWM0SNczej_qsPA5LXGykdVVT4jo4Nia-fwVwCDvhoXyFprHLmQM38uHFfanbJy0v-GZqlSuhVccRFhI3UfV6mbCNMaWHcq--onxuODiyj0CSwJ2d3cHEN_GlJJ8eVYnFCVXKXEC-vdxqzx69tbYSWeORmTixZOIzr3xU7VkcR0oZZyK4KwCXjpXM3VPcui308qWfZNsYvuvaXjGhZyv5XkM27odKH5y9BavOq38tfnnoyuPfl2JudeMOSfcS4AaORtD61pb2jHqpeLxTOb-jGpO3zSjQrroM09_oP4tLBUR8uv-CVNbCSrkP-Y9pne-gQF_Wn6DTa9E4kpxMvyUQgykPdiNRtnaqrI3b0AnOCzjKfnzOb-VrmwS5BuqtPlpBj-eOOnzKJqARgvAj0osZ4fc-OqNIXNNqBbo2qofbcPQ6s1mF7jg_cCEBWbRAoCSUgINdeEz5vFG2HqHArzgjJyqzvIHQdtBKD2hW8WcmV1sGsGynrBBQ5v8l_sMZ_RH_b2A5iRbAz3sDz2aMnDPaSo2OWb3HJb6tS_oh4Obc8kh2waYveSUkpawHQN7zVWJgl8c4by-tb_xXeWRxgPzXSb7ZMmCST9CGoRauNsmWlaa4MVtlQ8NBb3Ot-9KEt84IqzPmKvQ-n4FRhbY1K9MExgcQtNEhmXp13WP_CRAt7SB04TMWQxRgse5od-E3v2-nBAWVVpjjoNd0iShl8HVnRhTn-gMDPpSthfIy3_6uN9R-MWRkTmtdQIaj28c9KuQS2tS5q_nMg0z4wWoUbAqis8eX8WI3nmqGZa_DlZQGTHA"> </form> </div> </div> <script> (function(){ window._cf_chl_opt={ cvId: '2', cZone: 'lemmynsfw.com', cType: 'managed', cNounce: '33271', cRay: '7e01ac6c0e082bf0', cHash: 'a37b14d5276299f', cUPMDTk: "\/api\/v3\/user\/login?__cf_chl_tk=Szj2ardQ30.b0BUlYIuAfNYgXp3GwR.Al4jFLbPMTh8-1688246222-0-gaNycGzNCLs", cFPWv: 'g', cTTimeMs: '1000', cMTimeMs: '0', cTplV: 5, cTplB: 'cf', cK: "", cRq: { ru: 'aHR0cHM6Ly9sZW1teW5zZncuY29tL2FwaS92My91c2VyL2xvZ2lu', ra: 'YW5kcm9pZDpsdGQudWNvZGUuc2xpZGU6djAuMC4y', rm: 'UE9TVA==', d: 'eLiMVOG0kCYARV7Y0J5wb3c1CxwUEluUvLlcyfwy9lzFIAygDCSBIao1QpOX6F0Ok87R4UlAZTsLoFpXG07w/3TibFraqb1YPwR9Op8QTYBeMvMSbX2IrPMT/uGfSQ+FMuPX6iJRFs55C6HOnGH4LROu2W1XANGIg4L0cxPGhywQ/0yubQbzE5k80nBplRH/X9o/28XF7wZWosr3gs5RbFTwG/ddgmBEy3sZSbomLkuPRHFlqIcQJX8RQ6bVpw2IIVu5MdChbsGwoxiskqXs42VKZsbDQ5/oCoxThdE8VJTdoxmz+2QQL92Wtm1kmkaeRWFsqlJwYGi4/q7GU6aQCb+MefvKVxrHRU32cZewt3Id2vyNgiZGMsy5WSQxqV9SgT3CvkKRfhyk/pdci5iHspMIhNb/c9F+0X58ge2KREdZrP8am33fVjLcTBZS3m3hVj3dLOKB3TD2dLlYBfEekICfdhsHZqTkRjl3D3vARE5wbqcJD5Q5UYCw38m+QQEU9Y5wThRNoqelBXOE+QWWnJFEGzEGFcnO0Ql7OgbObpE=', t: 'MTY4ODI0NjIyMi43MjEwMDA=', cT: Math.floor(Date.now() / 1000), m: 'uD3k2Ta2mpTJci+GkeKeGRYSbkIFGZZGd69OUSgfAjg=', i1: 'hGvnPkSXxKhtXBeCL9wQ/g==', i2: 'o3bE3EfGujXGpuD+AfY17w==', zh: 'Wx8oBWXdAJvFOI2JS58yROUAyIQMzN3Yg5puIWGd9bs=', uh: '+4rDpGBuI12e41XlrYdr5SK9hKoNciDEt9BuE/Lw4Fw=', hh: 'XYzoJxfsPpc0mdk87tvqeCz6OMQCBWk+LFKjWwSPSKs=', } }; var trkjs = document.createElement('img'); trkjs.setAttribute('src', '/cdn-cgi/images/trace/managed/js/transparent.gif?ray=7e01ac6c0e082bf0'); trkjs.setAttribute('alt', ''); trkjs.setAttribute('style', 'display: none'); document.body.appendChild(trkjs); var cpo = document.createElement('script'); cpo.src = '/cdn-cgi/challenge-platform/h/g/orchestrate/managed/v1?ray=7e01ac6c0e082bf0'; window._cf_chl_opt.cOgUHash = location.hash === '' && location.href.indexOf('#') !== -1 ? '#' : location.hash; window._cf_chl_opt.cOgUQuery = location.search === '' && location.href.slice(0, location.href.length - window._cf_chl_opt.cOgUHash.length).indexOf('?') !== -1 ? '?' : location.search; if (window.history && window.history.replaceState) { var ogU = location.pathname + window._cf_chl_opt.cOgUQuery + window._cf_chl_opt.cOgUHash; history.replaceState(null, null, "\/api\/v3\/user\/login?__cf_chl_rt_tk=Szj2ardQ30.b0BUlYIuAfNYgXp3GwR.Al4jFLbPMTh8-1688246222-0-gaNycGzNCLs" + window._cf_chl_opt.cOgUHash); cpo.onload = function() { history.replaceState(null, null, ogU); }; } document.getElementsByTagName('head')[0].appendChild(cpo); }()); </script> </body> </html>
            """.trimIndent()
        )

        for (html in testHtml) {
            html.let(Jsoup::parse).wholeText().trim().replace(Regex(" +"), " ").let(::println)
        }
    }

}