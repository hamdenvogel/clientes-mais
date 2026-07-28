import { Directive, ElementRef, HostListener } from '@angular/core';

@Directive({
  selector: '[appOnlyNumbers]'
})
export default class OnlyNumbersDirective {

  private specialKeys = [
    'Backspace', 'Delete', 'Tab', 'Enter', 'Escape', 'Home',
    'End', 'ArrowLeft', 'ArrowRight', 'Clear', 'Copy', 'Paste'
  ];
  inputElement: HTMLElement;

  constructor(public el: ElementRef) {
    this.inputElement = el.nativeElement;
  }

  // Ao pressionar teclas que produzem ou n�o produzem um caractere
  @HostListener('keydown', ['$event'])
  onKeyDown(e: KeyboardEvent) {
    if (
      this.specialKeys.indexOf(e.key) > -1 || // Permite specialKeys: backspace, delete, arrows etc.
      (e.key === 'a' && e.ctrlKey === true) || // Permite: Ctrl+A
      (e.key === 'c' && e.ctrlKey === true) || // Permite: Ctrl+C
      (e.key === 'v' && e.ctrlKey === true) || // Permite: Ctrl+V
      (e.key === 'x' && e.ctrlKey === true) || // Permite: Ctrl+X
      (e.key === 'a' && e.metaKey === true) || // Permite: Cmd+A (Mac)
      (e.key === 'c' && e.metaKey === true) || // Permite: Cmd+C (Mac)
      (e.key === 'v' && e.metaKey === true) || // Permite: Cmd+V (Mac)
      (e.key === 'x' && e.metaKey === true) // Permite: Cmd+X (Mac)
    ) {
      // Ok
      return;
    }
    // Permite somente digitos quando a tecla gera caractere visivel.
    if (e.key.length === 1 && !/^\d$/.test(e.key)) {
      // Cancela o pressionamento da tecla
      e.preventDefault();
    }
  }

  // Ao pressionar teclas que produzem um caractere
  @HostListener('keypress', ['$event'])
  onKeyPress(e: KeyboardEvent) {
    // Bloqueia caracteres como: !@#$%&*()/ e qualquer nao digito.
    if (e.key.length === 1 && !/^\d$/.test(e.key)) {
      // Cancela o pressionamento da tecla
      e.preventDefault();
    }
  }

  // Ao colar valor
  @HostListener('paste', ['$event'])
  onPaste(event: ClipboardEvent) {
    event.preventDefault();
    const pastedInput: string = event.clipboardData
      .getData('text/plain')
      .replace(/\D/g, ''); // Remove caracteres n�o num�ricos
    document.execCommand('insertText', false, pastedInput);
  }

  // Ao arrastar valor
  @HostListener('drop', ['$event'])
  onDrop(event: DragEvent) {
    event.preventDefault();
    const textData = event.dataTransfer.getData('text').replace(/\D/g, '');
    this.inputElement.focus();
    document.execCommand('insertText', false, textData);
  }

}
