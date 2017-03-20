from PIL import Image
def manipulate(r):
	path = 'file{}.png'.format(r)
	f = Image.open(path)
	f = f.convert('1')
	f.save('result{}.png'.format(r))