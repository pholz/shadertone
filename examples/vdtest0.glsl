uniform float iOvertoneVolume;

float hz(float hz)
{
    float u = hz/22050.0;
    return texture2D(iChannel0,vec2(u,0.25)).x;
}

int sector(vec2 uv)
{
    int col; int row; int res;
    col = int (uv.x * 4);
    row = int (uv.y * 4);
    res = int (4 * row + col);
    return res;
}

void main (void)
{

    float hza[16];

    vec2 vUV = (gl_FragCoord.xy / iResolution.xy);

    for( int i = 0; i < 16; i++)
    {
        hza[i] = hz(pow(i+1,2)*100.0);
    }

    int sec = sector(vUV);
    //  sec = int(min(3, sec));
    float colorval = hza[sec];
    gl_FragColor = vec4(colorval, colorval, colorval, 1.0); // texture2D(iChannel0, vec2(vUV.x, 0.25));
}
