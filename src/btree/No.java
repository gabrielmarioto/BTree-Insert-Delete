/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btree;

import static btree.Ordem.m;

/**
 *
 * @author Gabriel
 */
public class No implements Ordem
{
    private int[] vInfo;
    private No[] vLig;
    private int[] vPos;
    private int tl;
    
    public No()
    {     
        vLig = new No[2*m + 2];
        vInfo = new int[2*m + 1];
        vPos = new int[2*m + 1];
    }
    
    public No(int info, int posArq)
    {
        this();
        vInfo[0] = info;
        vPos[0] = posArq;
        tl = 1;
    }
    
    public int getvInfo(int p)
    {
        return vInfo[p];
    }

    public void setvInfo(int p, int info)
    {
        vInfo[p] = info;
    }

    public No getvLig(int p)
    {
        return vLig[p];
    }

    public void setvLig(int p, No lig)
    {
        vLig[p] = lig;
    }

    public int getvPos(int p)
    {
        return vPos[p];
    }

    public void setvPos(int p, int posArq)
    {
        vPos[p] = posArq;
    }

    public int getTl()
    {
        return tl;
    }

    public void setTl(int tl)
    {
        this.tl = tl;
    }
    
    public void remanejar(int pos) // INSERIR
    {
        vLig[tl+1] = vLig[tl];
        for (int i = tl; i > pos; i--)
        {
            vInfo[i] = vInfo[i-1]; 
            vPos[i] = vPos[i-1];
            vLig[i] = vLig[i-1];                 
        }
    }
    
    public void remanejarExclusao(int pos)
    {
        for (int i = pos; i < tl-1; i++)
        {
            vInfo[i] = vInfo[i+1];
            vPos[i] = vPos[i+1];
            vLig[i] = vLig[i+1]; 
        }
        vLig[tl-1] = vLig[tl];
        tl--;
    }
    
    public int procurarPosicao(int info)
    {
        int pos = 0;
        while(pos < tl && info > vInfo[pos])
            pos++;
        
        return pos;
    }
    
    public void insert(int info, int posArq)
    {
        int pos = procurarPosicao(info);
        remanejar(pos);
        vInfo[pos] = info;
        vPos[pos] = posArq;
        tl++;
    }
    
    public void swap (int pos, int info, int posArq)
    {
        vInfo[pos] = info;
        vPos[pos] = posArq;
    }
}
