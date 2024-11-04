param ([string]$processName)

# Añadir el tipo User32 para usar ShowWindow
Add-Type @"
using System;
using System.Runtime.InteropServices;
public class User32 {
    [DllImport("user32.dll", SetLastError = true, CharSet = CharSet.Auto)]
    public static extern bool ShowWindow(IntPtr hWnd, int nCmdShow);
    public const int SW_MINIMIZE = 6;
}
"@

# Obtener el proceso
try {
    $process = Get-Process -Name $processName -ErrorAction Stop
    [User32]::ShowWindow($process.MainWindowHandle, [User32]::SW_MINIMIZE)
} catch {
    Write-Output "No se encontró el proceso: $processName"
}
